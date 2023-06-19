<template>
  <div v-show="gptFlag">
    <!-- 聊天界面 -->
    <div
        class="chat-container animated bounceInUp"
    >
      <!-- 标题 -->
      <div class="header">
        <img
            width="32"
            height="32"
            :src="this.$store.state.blogInfo.websiteConfig.chatGptAvatar"
        />
        <div style="margin-left:12px">
          <div>你的智能助手</div>
        </div>
        <v-icon class="close" @click="gptFlag = false">
          mdi-close
        </v-icon>
      </div>
      <!-- 对话内容 -->
      <div class="message" id="message">
        <div
            :class="isMyMessage(item)"
            v-for="(item, index) of chatRecordList"
            :key="index"
        >
          <!-- 头像 -->
          <img :src="item.avatar" :class="isleft(item)" />
          <div>
            <div class="nickname" v-if="!isSelf(item)">
              你的助手
              <span style="margin-left:12px">{{ item.createTime | hour }}</span>
            </div>
            <!-- 内容 -->
            <div
                ref="content"
                :class="isMyContent(item)"
            >
              <!-- 文字消息 -->
              <article v-html="item.content" class="markdown-body"/>
            </div>
          </div>
        </div>
      </div>
      <!-- 输入框 -->
      <div class="footer">
        <!-- 文字输入 -->
        <textarea
            ref="chatInput"
            v-model="content"
            @keydown.enter="sendMessage($event)"
            placeholder="请输入问题"
        />
        <!-- 发送按钮 -->
        <i :class="isInput" @click="sendMessage" style="font-size: 1.5rem" />
      </div>
    </div>
  </div>
</template>

<script>
import Recorderx, { ENCODE_TYPE } from "recorderx";
import Emoji from "./Emoji";
import EmojiList from "../assets/js/emoji";
import Article from "@/views/article/Article";
export default {
  components: {
    Article,
    Emoji
  },
  updated() {
    var ele = document.getElementById("message");
    ele.scrollTop = ele.scrollHeight;
  },
  data: function() {
    return {
      content: "",  // 对话框输入内容
      chatRecordList: [],  // 聊天历史记录
      ipAddress: "",  // ip地址
      ipSource: "",   // ip资源位置
    };
  },
  methods: {
    // 打开聊天框
    open() {
      if (this.websocket == null) {
        this.connect();
      }
      this.unreadCount = 0;
      this.gptFlag = !this.gptFlag;
    },
    // 发送消息
    sendMessage(event){
      // 阻止按回车默认换行
      event.preventDefault();
      // 判空
      if (this.content.trim() === "") {
        this.$toast({ type: "error", message: "内容不能为空" });
        return false;
      }

      // 将提问的消息发送上去
      const tempContent = this.content;
      this.content = '';
      // 获取当前用户的头像,登陆了就使用用户头像
      let userAvatar;
      if (this.$store.state.avatar){
        userAvatar = this.$store.state.avatar
      }else {
        userAvatar = this.$store.state.blogInfo.websiteConfig.touristAvatar;
      }
      const userChat = {
        role: 'user',
        content: tempContent,
        avatar: userAvatar,
        name: null
      }
      this.chatRecordList.push(userChat)

      // GPT显示正在发送
      const gptChat = {
        role: 'assistant',
        content: '正在生成回答，请等待。。。',
        avatar: this.$store.state.blogInfo.websiteConfig.chatGptAvatar,
        name: null
      }
      this.chatRecordList.push(gptChat)

      const eventSource = new EventSource(`/api/sendStream/${tempContent}`);
      // const eventSource = new EventSource(`/api/stream3/${tempContent}`);

      let that = this;

      eventSource.onopen = function () {
        console.log('SSE建立连接成功');
        // 添加一个回答的数据
        console.log(that.chatRecordList);
        that.chatRecordList[that.chatRecordList.length - 1].content = '';
      }

      // 接收消息
      eventSource.onmessage = function (event) {
        // console.log("接收到数据event" + event);
        // console.log(event.data);
        if (event.data && typeof event.data === 'string'){
          const data = JSON.parse(event.data);
          // console.log(data);
          const reMessage = data.delta.content;
          // console.log(reMessage);
          // 将查询到的数据添加到最新的回答中
          const lastIndex = that.chatRecordList.length - 1;
          const chat = that.chatRecordList[lastIndex];
          chat.content += reMessage;
          that.$set(that.chatRecordList, lastIndex, chat)
        }
      }

      eventSource.onerror = function (err) {
        console.log('SSE连接异常,关闭连接');
        // console.log(err);
        // console.log(err.eventPhase);
        // console.log(err.currentTarget.readyState);
        // console.log(err.currentTarget.status);
        eventSource.close();
        // const lastIndex = this.chatRecordList.length - 1;
        // const chat = this.chatRecordList[lastIndex];
        // chat.content = '出现错误，可能是网络出现问题';
        // this.$set(this.chatRecordList, lastIndex, chat)
      }

      // 向GPT提问
      // this.$axios.get("/api/send", {
      //   params: {
      //     message: tempContent
      //   }
      // }).then(({data}) => {
      //   console.log(data);
      //   if (data.code !== 20000){
      //     this.$set(this.chatRecordList, this.chatRecordList.length-1, data.message)
      //     return;
      //   }
      //   // 将生成回答中替换成最后的结果
      //   // this.chatRecordList.push(data)
      //   this.$set(this.chatRecordList, this.chatRecordList.length-1, data.data)
      // })
    }
  },
  computed: {
    gptFlag: {
      set(value) {
        this.$store.state.gptFlag = value;
      },
      get() {
        return this.$store.state.gptFlag;
      }
    },
    isSelf() {
      return function(item) {
        return (
            item.role === 'user'
        );
      };
    },
    isleft() {
      return function(item) {
        return this.isSelf(item)
            ? "user-avatar right-avatar"
            : "user-avatar left-avatar";
      };
    },
    isMyContent() {
      return function(item) {
        return this.isSelf(item) ? "my-content" : "user-content markdown-body";
      };
    },
    isMyMessage() {
      return function(item) {
        return this.isSelf(item) ? "my-message" : "user-message";
      };
    },
    blogInfo() {
      return this.$store.state.blogInfo;
    },
    // 如果登录了就设置成当前用户名称，不然设置成ip地址
    nickname() {
      return this.$store.state.nickname != null
          ? this.$store.state.nickname
          : this.ipAddress;
    },
    avatar() {
      return this.$store.state.avatar != null
          ? this.$store.state.avatar
          : this.$store.state.blogInfo.websiteConfig.touristAvatar;
    },
    userId() {
      return this.$store.state.userId;
    },
    isInput() {
      return this.content.trim() !== ""
          ? "iconfont iconzhifeiji submit-btn"
          : "iconfont iconzhifeiji";
    }
  }
};
</script>

<style scoped>
@media (min-width: 760px) {
  .chat-container {
    position: fixed;
    color: #4c4948 !important;
    bottom: 104px;
    left: 25%;
    height: calc(85% - 64px - 20px);
    max-height: 590px !important;
    min-height: 250px !important;
    width: 800px !important;
    border-radius: 16px !important;
  }
  .close {
    display: block;
    margin-left: auto;
  }
}
@media (max-width: 760px) {
  .chat-container {
    position: fixed;
    top: 0;
    bottom: 0;
    right: 0;
    left: 0;
  }
  .close {
    display: block;
    margin-left: auto;
  }
}
.chat-container {
  box-shadow: 0 5px 40px rgba(0, 0, 0, 0.16) !important;
  font-size: 14px;
  background: #f4f6fb;
  z-index: 1200;
}
.chat-btn {
  background: #fff;
  /*background: #1f93ff;*/
  border-radius: 100px !important;
  position: fixed;
  bottom: 15px;
  right: 5px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.16) !important;
  cursor: pointer;
  height: 60px !important;
  width: 60px !important;
  z-index: 1000 !important;
  user-select: none;
}
.header {
  display: flex;
  align-items: center;
  padding: 20px 24px;
  background: #ffffff;
  border-radius: 1rem 1rem 0 0;
  box-shadow: 0 10px 15px -16px rgba(50, 50, 93, 0.08),
  0 4px 6px -8px rgba(50, 50, 93, 0.04);
}
.footer {
  padding: 8px 16px;
  position: absolute;
  width: 100%;
  bottom: 0;
  background: #f7f7f7;
  border-radius: 0 0 1rem 1rem;
  display: flex;
  align-items: center;
}
.footer textarea {
  background: #fff;
  padding-left: 10px;
  padding-top: 8px;
  width: 100%;
  height: 32px;
  outline: none;
  resize: none;
  overflow: hidden;
  font-size: 13px;
}
.voice-btn {
  font-size: 13px;
  outline: none;
  height: 32px;
  width: 100%;
  background: #fff;
  border-radius: 2px;
}
.message {
  position: absolute;
  width: 100%;
  padding: 20px 16px 0 16px;
  top: 80px;
  bottom: 50px;
  overflow-y: auto;
  overflow-x: hidden;
}
.text {
  color: #999;
  text-align: center;
  font-size: 12px;
  margin-bottom: 12px;
}
.user-message {
  display: flex;
  margin-bottom: 10px;
}
.my-message {
  display: flex;
  margin-bottom: 10px;
  justify-content: flex-end;
}
.left-avatar {
  margin-right: 10px;
}
.right-avatar {
  order: 1;
  margin-left: 10px;
}
.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.nickname {
  display: flex;
  align-items: center;
  font-size: 12px;
  margin-top: 3px;
  margin-bottom: 5px;
}
.user-content {
  position: relative;
  background-color: #fff;
  padding: 10px;
  border-radius: 5px 20px 20px 20px;
  width: fit-content;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
.my-content {
  position: relative;
  border-radius: 20px 5px 20px 20px;
  padding: 12px;
  background: #12b7f5;
  color: #fff;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
.submit-btn {
  color: rgb(31, 147, 255);
}
.emoji {
  cursor: pointer;
  font-size: 1.3rem;
  margin: 0 8px;
}
.emoji-box {
  position: absolute;
  box-shadow: 0 8px 16px rgba(50, 50, 93, 0.08), 0 4px 12px rgba(0, 0, 0, 0.07);
  background: #fff;
  border-radius: 8px;
  right: 20px;
  bottom: 52px;
  height: 180px;
  width: 300px;
  overflow-y: auto;
  padding: 6px 16px;
}
.emoji-border:before {
  display: block;
  height: 0;
  width: 0;
  content: "";
  border-left: 14px solid transparent;
  border-right: 14px solid transparent;
  border-top: 12px solid #fff;
  bottom: 40px;
  position: absolute;
  right: 43px;
}
.unread {
  text-align: center;
  border-radius: 50%;
  font-size: 14px;
  height: 20px;
  width: 20px;
  position: absolute;
  background: #f24f2d;
  color: #fff;
}
.back-menu {
  font-size: 13px;
  border-radius: 2px;
  position: absolute;
  background: rgba(255, 255, 255, 0.9);
  color: #000;
  width: 80px;
  height: 35px;
  text-align: center;
  line-height: 35px;
  display: none;
}
.voice {
  position: fixed;
  z-index: 1500;
  bottom: 52px;
  left: 0;
  right: 0;
  top: 80px;
  background: rgba(0, 0, 0, 0.8);
}
.close-voice {
  position: absolute;
  bottom: 60px;
  left: 30px;
  display: inline-block;
  height: 50px;
  width: 50px;
  line-height: 50px;
  border-radius: 50%;
  text-align: center;
  background: #fff;
}
</style>
