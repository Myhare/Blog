<template>
  <div>
    <!-- banner -->
    <div class="message-banner" :style="cover">
      <!-- 弹幕输入框 -->
      <div class="message-container">
        <h1 class="message-title">留言板</h1>
        <div class="animated fadeInUp message-input-wrapper">
          <input
              v-model="messageContent"
              @click="show = true"
              @keyup.enter="addToList"
              placeholder="说点什么吧"
          />
          <button
              class="ml-3 animated bounceInLeft"
              @click="addToList"
              v-show="show"
          >
            发送
          </button>
        </div>
      </div>
      <!-- 弹幕列表 -->
      <div class="barrage-container">
        <template>
          <div>
            <vue-danmaku ref="danmaku" v-model="barrageList" style="width: 1550px; height: 1550px" use-slot :speeds="200" :channels="5">
              <!-- 弹幕插槽（vue 2.6.0 及以上版本可使用 v-slot:dm="{ index, danmu }"语法） -->
              <template slot="dm" slot-scope="{ index, danmu }">
                  <span class="barrage-items">
                    <img
                        :src="danmu.avatar"
                        width="30"
                        height="30"
                        style="border-radius:50%"
                    />
                    <span class="ml-2">{{ danmu.nickname }} :</span>
                    <span class="ml-2">{{ danmu.messageContent }}</span>
                  </span>
              </template>
              <!-- 容器插槽 -->
              <div></div>
            </vue-danmaku>
          </div>
        </template>
        <!--        <vue-baberrage :barrageList="barrageList">-->
        <!--          <template v-slot:default="slotProps">-->
        <!--            <span class="barrage-items">-->
        <!--              <img-->
        <!--                :src="slotProps.item.avatar"-->
        <!--                width="30"-->
        <!--                height="30"-->
        <!--                style="border-radius:50%"-->
        <!--              />-->
        <!--              <span class="ml-2">{{ slotProps.item.nickname }} :</span>-->
        <!--              <span class="ml-2">{{ slotProps.item.messageContent }}</span>-->
        <!--            </span>-->
        <!--          </template>-->
        <!--        </vue-baberrage>-->
      </div>
    </div>
  </div>
</template>

<script>
import vueDanmaku from 'vue-danmaku'
export default {
  components: {
    vueDanmaku,
  },
  mounted() {
    this.listMessage();
  },
  data() {
    return {
      show: false,
      messageContent: "",
      barrageList: []
    };
  },
  methods: {
    // 添加列表
    addToList() {
      if (this.messageContent.trim() === "") {
        this.$toast({ type: "error", message: "留言不能为空" });
        return false;
      }
      const userAvatar = this.$store.state.avatar
          ? this.$store.state.avatar
          : this.$store.state.blogInfo.websiteConfig.touristAvatar;
      const userNickname = this.$store.state.nickname
          ? this.$store.state.nickname
          : "游客";
      var message = {
        avatar: userAvatar,
        nickname: userNickname,
        messageContent: this.messageContent,
        time: Math.floor(Math.random() * (10 - 7)) + 7 // 弹幕速度
      };
      this.messageContent = "";
      this.$axios.post("/api/messages", message).then(({ data }) => {
        if (data.flag) {
          this.barrageList.push(message);
          this.$toast({ type: "success", message: "留言成功" });
        } else {
          this.$toast({ type: "error", message: data.message });
        }
      });
    },
    // 获取消息列表
    listMessage() {
      this.$axios.get("/api/messages").then(({ data }) => {
        if (data.flag) {
          this.barrageList = data.data;
          console.log(this.barrageList);
        }
      });
    }
  },
  computed: {
    cover() {
      var cover = "";
      this.$store.state.blogInfo.pageList.forEach(item => {
        if (item.pageLabel === "message") {
          cover = item.pageCover;
        }
      });
      return "background: url(" + cover + ") center center / cover no-repeat";
    }
  }
};
</script>

<style scoped>
.message-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 100vh;
  background-color: #49b1f5;
  animation: header-effect 1s;
}
.message-title {
  color: #eee;
  animation: title-scale 1s;
}
.message-container {
  position: absolute;
  width: 360px;
  top: 35%;
  left: 0;
  right: 0;
  text-align: center;
  z-index: 5;
  margin: 0 auto;
  color: #fff;
}
.message-input-wrapper {
  display: flex;
  justify-content: center;
  height: 2.5rem;
  margin-top: 2rem;
}
.message-input-wrapper input {
  outline: none;
  width: 70%;
  border-radius: 20px;
  height: 100%;
  padding: 0 1.25rem;
  color: #eee;
  border: #fff 1px solid;
}
.message-input-wrapper input::-webkit-input-placeholder {
  color: #eeee;
}
.message-input-wrapper button {
  outline: none;
  border-radius: 20px;
  height: 100%;
  padding: 0 1.25rem;
  border: #fff 1px solid;
}
.barrage-container {
  position: absolute;
  top: 50px;
  left: 0;
  right: 0;
  bottom: 0;
  height: calc(100% -50px);
  width: 100%;
}
.barrage-items {
  background: rgba(56, 51, 51, 0.7);
  /*background: rgba(0, 0, 0, 0.7);*/
  border-radius: 100px;
  color: #fff;
  padding: 5px 10px 5px 5px;
  align-items: center;
  display: flex;
}
</style>
