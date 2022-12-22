import {
  login,
  logout,
  getInfo,
  getUserList,
  updateUserDelete,
  changeUserInfo,
  getOnline
} from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import store from "@/store";

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  // 设置用户简单信息
  SET_USERINFO: (state,userInfo) => {
    const { nickName, intro } = userInfo;
    state.name = nickName;
    state.introduction = intro;
  }
}

const actions = {
  // 用户登录
  login({ commit }, userInfo) {
    const { username, password } = userInfo;
    let param = new URLSearchParams();
    param.append("username", username);
    param.append("password", password);
    return new Promise((resolve, reject) => {
      login(param).then(response => {
        const { data : token } = response
        // console.log("登录成功response");
        // console.log(response);
        // console.log("登录成功返回token")
        // console.log(token);  // 这里得data就是返回的token
        commit('SET_TOKEN', token)

        // const { roleList, name, avatar, intro} = data.userInfo;
        // commit('SET_ROLES', roleList)
        // commit('SET_NAME', name)
        // commit('SET_AVATAR', avatar)
        // commit('SET_INTRODUCTION', intro)

        // 设置Cookies中的token
        setToken(token)
        resolve()
      }).catch(error => {
        // console.log("登录失败error");
        // console.log(error);
        // console.log("登录失败，接下来进入reject");
        reject(error)
      })
    })
  },

  // 获取用户信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        // console.log("获取用户信息getInfo返回response：");
        // console.log(response);
        const { data } = response

        if (!data) {
          reject('验证失败，请重新登录.')
        }
        // console.log("getInfo中返回的data：");
        // console.log(data);
        const { roles, name, avatar, introduction } = data

        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo：角色必须是非空数组！')
        }

        // 将查询到的用户基本信息存入vuex
        commit('SET_ROLES', roles)
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        commit('SET_INTRODUCTION', introduction)
        resolve(data)
      }).catch(error => {
        reject(error)
      })

    })

  },

  // 修改用户基本信息
  changeUserInfo({ commit},userInfo){
    return new Promise((resolve, reject) => {
      // console.log(userInfo);
      const{ nickName , intro } = userInfo;
      changeUserInfo(nickName,intro).then(response=>{
        console.log("changeUserInfo中返回response");
        console.log(response);
        const {flag,message} = response;
        if (!flag) {
          reject('修改用户基本信息失败')
        }
        resolve(message);
      }).catch(error => {
        reject(error);
      })
    });
  },


  // 用户登出
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // 重置访问的视图和缓存的视图
        // 至固定 https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 删除token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // 动态修改权限
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  },

  // 分页获取用户列表
  async getUserList(context,queryInfo){
    // console.log("成功进入vuex中的getUserList方法,context:");
    // console.log(context);
    // console.log("成功进入vuex中的getUserList方法,queryInfo:");
    // console.log(queryInfo);
    return new Promise((resolve, reject) => {
      getUserList(queryInfo).then(response => {
        console.log("获取用户信息成功");
        console.log(response);
        const { data } = response;
        if (!data){
          reject('获取用户信息失败');
        }
        resolve(data);
      }).catch(error=>{
        reject(error);
      })
    })
  },

  // 禁止/解除禁止某一个用户
  async updateUserDelete(context,param){
    const {userId,isDelete} = param;
    console.log("updateUserDelete获取到的userId-->"+userId);
    console.log("updateUserDelete获取到的isDelete-->"+isDelete);
    return new Promise(((resolve, reject) => {
      updateUserDelete(userId,isDelete).then(response=>{
        console.log("修改用户状态成功");
        console.log(response);
        const {data} = response;
        if (!data){
          reject('修改用户状态失败');
        }
        resolve(data);
      }).catch(error=>{
        reject(error);
      })
    }))
  },

  // 获取在线用户信息
  getOnline(context,params){
    return new Promise(resolve => {
      getOnline(params).then(res => {
        console.log("获取在线用户列表成功");
        console.log(res);
        resolve(res.data);
      })
    })
  }

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
