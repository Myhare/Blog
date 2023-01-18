<template>
  <v-dialog v-model="registerFlag" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="registerFlag = false">
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
        <!-- 密码 -->
        <v-text-field
          v-model="password"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          @keyup.enter="register"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
        />
        <!-- 注册按钮 -->
        <v-btn
          class="mt-7"
          block
          color="red"
          style="color:#fff"
          @click="register"
        >
          注册
        </v-btn>
        <!-- 登录 -->
        <div class="mt-10 login-tip">
          已有账号？<span @click="openLogin">登录</span>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: function() {
    return {
      email: "",
      code: "",
      password: "",
      flag: true,
      codeMsg: "发送",
      time: 60,
      show: false
    };
  },
  methods: {
    openLogin() {
      this.$store.state.registerFlag = false;
      this.$store.state.loginFlag = true;
    },
    // 发送验证码
    sendCode() {
      const that = this;
      // // eslint-disable-next-line no-undef
      // var captcha = new TencentCaptcha(this.config.TENCENT_CAPTCHA, function(
      //   res
      // ) {
      //   console.log('发送邮件验证码:');
      //   console.log(res);
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
                that.$toast({ type: "success", message: "发送成功" });
              } else {
                that.$toast({ type: "error", message: data.message });
              }
            });
        // }
      // });
      // // 显示验证码
      // captcha.show();
    },
    // 倒计时
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
    // 注册
    register() {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (!reg.test(this.email)) {
        this.$toast({ type: "error", message: "邮箱格式不正确" });
        return false;
      }
      if (this.code.trim().length != 6) {
        this.$toast({ type: "error", message: "请输入6位验证码" });
        return false;
      }
      if (this.password.trim().length < 6) {
        this.$toast({ type: "error", message: "密码不能少于6位" });
        return false;
      }
      const user = {
        email: this.email,
        password: this.password,
        code: this.code
      };
      this.$axios.post("/api/register", user).then(({ data }) => {
        if (data.flag) {
          let param = new URLSearchParams();
          param.append("username", user.email);
          param.append("password", user.password);
          param.append("isFront", true);  // 给一个标记，表示这个是前端登录
          this.$axios.post("/api/login", param).then(({ data }) => {
            console.log('登录接口返回对象data:');
            console.log(data);
            this.email = "";
            this.password = "";
            this.$store.commit("LOGIN", data.data);
            this.$store.commit("CLOSE_MODEL");
          });
          this.$toast({ type: "success", message: "登录成功" });
        } else {
          this.$toast({ type: "error", message: data.message });
        }
      });
    }
  },
  computed: {
    registerFlag: {
      set(value) {
        this.$store.state.registerFlag = value;
      },
      get() {
        return this.$store.state.registerFlag;
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
