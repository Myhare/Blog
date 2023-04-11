import {
  logList,
  delLogList
} from "@/api/log";


const state = {

}

const actions = {
  // 查询日志列表
  logList(context,queryInfo){
    return new Promise((resolve => {
      logList(queryInfo).then(res => {
        resolve(res);
      })
    }))
  },

  // 删除日志列表
  delLogList(context, logIdList){
    return new Promise(resolve => {
      delLogList(logIdList)
        .then(res => {
          resolve(res);
        })
    })
  }
}

export default {
  namespaced: true,
  state,
  actions
}
