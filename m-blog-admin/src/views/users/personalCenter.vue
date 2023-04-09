<template>
  <!--个人中心-->
  <div>
    <div class="personalCenter">
      <el-card class="main-card" style="position: relative">
        <el-tabs v-model="activeName" @tab-click="tabChange">
          <!--个人中心-->
          <el-tab-pane label="个人中心" name="userInfo">
            <div class="userInfoBox">
              <!--头像区域-->
              <div class="headBox">
                <button id="pick-avatar">
                  <img v-if="avatar" :src="avatar" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </button>
              </div>
              <!--trigger添加触发器-->
              <avatar-cropper
                @uploaded="uploadAvatar"
                upload-url="/api/user/avatar"
                trigger="#pick-avatar"
              />
              <!--用户信息表单区域-->
              <div class="formBox">
                <el-form label-position="right"
                         label-width="100px"
                         ref="userInfoFormRef"
                         :rules="userInfoRules"
                         :model="userInfo">
                  <el-form-item label="用户昵称" prop="nickName">
                    <el-input v-model="userInfo.nickName"></el-input>
                  </el-form-item>
                  <el-form-item label="用户简介" prop="intro">
                    <el-input v-model="userInfo.intro"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="submitForm(userInfo)">修改信息</el-button>
                    <el-button @click="resetForm()">重置</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>

          </el-tab-pane>
          <el-tab-pane label="修改密码" name="changePassword">
            <el-form label-width="70px" :model="passwordForm" style="width:320px">
              <el-form-item label="旧密码">
                <el-input
                  @keyup.enter.native="updatePassword"
                  v-model="passwordForm.oldPassword"
                  size="small"
                  show-password
                />
              </el-form-item>
              <el-form-item label="新密码">
                <el-input
                  @keyup.enter.native="updatePassword"
                  v-model="passwordForm.newPassword"
                  size="small"
                  show-password
                />
              </el-form-item>
              <el-form-item label="确认密码">
                <el-input
                  @keyup.enter.native="updatePassword"
                  v-model="passwordForm.confirmPassword"
                  size="small"
                  show-password
                />
              </el-form-item>
              <el-button
                type="primary"
                size="medium"
                style="margin-left:4.4rem"
                @click="updatePassword"
              >
                修改
              </el-button>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>

</template>

<script>
import AvatarCropper from "vue-avatar-cropper"

export default {
  name: "personalCenter",
  components:{ AvatarCropper },
  data(){
    return{
      activeName: 'userInfo',
      userInfo:{
        nickName:this.$store.getters.name,
        intro:this.$store.getters.introduction,
      },
      // 密码表单
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
      // 表单验证
      userInfoRules:{
        // 用户昵称不能为空
        nickName:[
          { required: true, message: '用户昵称不能为空', trigger: 'blur'}
        ],
        // 个人简介不能为空
        intro:[
          { required: true, message: '个人简介不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods:{
    // 切换标签
    tabChange(){
      console.log("切换标签");
    },
    // 上传表单
    submitForm(userInfo) {
      // console.log(userInfo);
      // 如果用户没有修改个人基本信息，不提交表单
      const {nickName,intro} = this.userInfo;
      if (nickName===this.$store.getters.name && intro === this.$store.getters.introduction){
        return;
      }
      this.$refs['userInfoFormRef'].validate((valid) => {
        if (valid) {
          this.$store.dispatch("user/changeUserInfo",this.userInfo)
            .then((message)=>{
              // console.log("修改用户信息成功,返回message:");
              // console.log(message);
              // 将用户信息更新到vuex中
              console.log("this.userInfo:");
              console.log(this.userInfo);
              const nickName = this.userInfo.nickName;
              const intro = this.userInfo.intro;
              this.$store.commit("user/SET_USERINFO",{
                nickName,
                intro
              });
              this.$message.success(message);
            })
        } else {
          console.log('表单验证失败');
          return false;
        }
      });
    },
    // 重置表单
    resetForm(){
      this.userInfo.nickName = this.$store.getters.name;
      this.userInfo.intro = this.$store.getters.introduction;
    },
    updatePassword() {
      if (this.passwordForm.oldPassword.trim() == "") {
        this.$message.error("旧密码不能为空");
        return false;
      }
      if (this.passwordForm.newPassword.trim() == "") {
        this.$message.error("新密码不能为空");
        return false;
      }
      if (this.passwordForm.newPassword.length < 6) {
        this.$message.error("新密码不能少于6位");
        return false;
      }
      if (this.passwordForm.newPassword != this.passwordForm.confirmPassword) {
        this.$message.error("两次密码输入不一致");
        return false;
      }
      this.$store.dispatch('user/updatePassword',this.passwordForm)
        .then(data => {
          if (data.flag) {
            this.passwordForm.oldPassword = "";
            this.passwordForm.newPassword = "";
            this.passwordForm.confirmPassword = "";
            this.$message.success(data.message);
          } else {
            this.$message.error(data.message);
          }
        })
    },
    // 更新头像后函数
    uploadAvatar(data){
      console.log("更新头像完成,data:");
      console.log(data);
      console.log(this.$store.state);
      if (data.flag){
        this.$store.state.user.avatar = data.data;
      }else {
        this.$message.error(data.message);
      }
    }
  },
  computed:{
    // 用户头像路径
    avatar(){
      return this.$store.getters.avatar;
    }
  }
}
</script>

<style scoped lang="scss">

.personalCenter{
  padding: 20px;
  .userInfoBox{
    display: flex;
    align-items: center;
    margin-left: 22%;
    margin-top: 7rem;
    //background-color: #dddddd;
    .headBox{
      border: 1px dashed #d9d9d9;  // 虚线边框
      border-radius: 6px;
      cursor: pointer;   // 鼠标移动上去会变成小手
      .avatar{
        width: 140px;
        height: 140px;
        display: block;
        border-radius: 50%;
      }
    }
  }
}

</style>
