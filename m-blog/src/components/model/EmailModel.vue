<template>
  <v-dialog v-model="emailFlag" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="emailFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field
          v-model="email"
          label="邮箱号"
          placeholder="请输入您的邮箱号"
          clearable
          @keyup.enter="register"
        />
        <!-- 验证码 -->
        <div class="mt-7 send-wrapper">
          <v-text-field
            maxlength="6"
            v-model="code"
            label="验证码"
            placeholder="请输入6位验证码"
            @keyup.enter="register"
          />
          <v-btn text small :disabled="flag" @click="sendCode">
            {{ codeMsg }}
          </v-btn>
        </div>
        <!-- 按钮 -->
        <v-btn
          class="mt-7"
          block
          color="blue"
          style="color:#fff"
          @click="saveUserEmail"
        >
          绑定
        </v-btn>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: function() {
    return {
      email: this.$store.state.email,
      code: "",
      flag: true,
      codeMsg: "发送",
      time: 60,
      show: false
    };
  },
  methods: {
    sendCode() {
      const that = this;
      // 校验当前输入的邮箱是不是用户当前的邮箱
      if (this.$store.state.email && this.$store.state.email === that.email){
        return that.$toast({type: "error",message: "已经绑定当前邮箱"})
      }
      // eslint-disable-next-line no-undef
      // var captcha = new TencentCaptcha(this.config.TENCENT_CAPTCHA, function(
      //   res
      // ) {
      //   if (res.ret === 0) {
          //发送邮件
          that.countDown();
          that.$axios
              .get("/api/sendEmail", {
                params: {
                  email: that.email
                }
              })
              .then(({ data }) => {
                if (data.flag) {
                  that.$toast({ type: "success", message: data.message });
                } else {
                  that.$toast({ type: "error", message: data.message });
                }
              });
      //   }
      // });
      // 显示验证码
      captcha.show();
    },
    countDown() {
      this.flag = true;
      this.timer = setInterval(() => {
        this.time--;
        this.codeMsg = this.time + "s";
        if (this.time <= 0) {
          clearInterval(this.timer);
          this.codeMsg = "发送";
          this.time = 60;
          this.flag = false;
        }
      }, 1000);
    },
    // 绑定用户邮箱
    saveUserEmail() {
      if (!this.checkEmail(this.email)){
        this.$toast({ type: "error", message: "邮箱格式不正确" });
      }
      if (this.code.trim().length !== 6) {
        this.$toast({ type: "error", message: "请输入6位验证码" });
        return false;
      }
      const user = {
        email: this.email,
        code: this.code
      };
      this.$axios.post("/api/users/email", user).then(({ data }) => {
        if (data.flag) {
          this.$store.commit("CHANGE_EMAIL", this.email);
          this.email = "";
          this.code = "";
          this.$store.commit("CLOSE_MODEL");
          this.$toast({ type: "success", message: data.message });
        } else {
          this.$toast({ type: "error", message: data.message });
        }
      });
    },
    // 验证邮箱格式是否正确
    checkEmail(email){
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      return reg.test(email);
    }
  },
  computed: {
    emailFlag: {
      set(value) {
        this.$store.state.emailFlag = value;
      },
      get() {
        return this.$store.state.emailFlag;
      }
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    }
  },
  watch: {
    email(value) {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (reg.test(value)) {
        this.flag = false;
      } else {
        this.flag = true;
      }
    }
  }
};
</script>

<style scoped>
@media (min-width: 760px) {
  .login-container {
    padding: 1rem;
    border-radius: 4px;
    height: 400px;
  }
}
</style>
