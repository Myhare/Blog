<template>
  <div style="padding: 20px">
    <el-card class="main-card">
      <div class="title">{{ this.$route.meta.title }}</div>
      <div class="talk-container">
        <!-- 输入框 -->
        <Editor
          ref="editor"
          class="editor-wrapper"
          v-model="talk.content"
          placeholder="说点什么吧"
        />
        <!-- 操作菜单 -->
        <div class="operation-wrapper">
          <div class="left-wrapper">
            <!-- 表情 -->
            <el-popover placement="bottom-start" width="460" trigger="click">
            <span
              class="emoji-item"
              v-for="(value, key, index) of emojiList"
              :key="index"
              @click="addEmoji(key, value)"
            >
              <img
                :src="value"
                :title="key"
                class="emoji"
                width="24"
                height="24"
              />
            </span>
              <i
                class="iconfont el-icon-mybiaoqing operation-btn"
                slot="reference"
              />
            </el-popover>
            <!-- 图片上传 -->
            <el-upload
              action="/api/talks/images"
              multiple
              :before-upload="beforeUpload"
              :on-success="upload"
              :show-file-list="false"
            >
              <i class="iconfont el-icon-mytupian operation-btn" />
            </el-upload>
          </div>
          <div class="right-wrapper">
            <!-- 是否置顶 -->
            <el-switch
              style="margin-right:16px"
              v-model="talk.isTop"
              inactive-text="置顶"
              :active-value="1"
              :inactive-value="0"
            />
            <!-- 说说状态 -->
            <el-dropdown
              trigger="click"
              @command="handleCommand"
              style="margin-right:16px"
            >
            <span class="talk-status">
              {{ dropdownTitle }}<i class="el-icon-arrow-down el-icon--right" />
            </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  v-for="(item, index) of statusList"
                  :key="index"
                  :command="item.status"
                >
                  {{ item.desc }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button
              type="primary"
              size="small"
              @click="saveOrUpdateTalk"
              :disabled="talk.content == ''"
            >
              发布
            </el-button>
          </div>
        </div>
        <!-- 图片上传 -->
        <el-upload
          class="talk-image-upload"
          v-show="uploadList.length > 0"
          action="/api/talks/images"
          list-type="picture-card"
          :file-list="uploadList"
          multiple
          :before-upload="beforeUpload"
          :on-success="upload"
          :on-remove="handleRemove"
        >
          <i class="el-icon-plus" />
        </el-upload>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as imageConversion from "image-conversion";
import EmojiList from "../../assets/js/emoji";
import Editor from "../../components/Editor.vue";
export default {
  components: {
    Editor
  },
  created() {
    if (this.$route.query.talkId) {
      this.$store.dispatch('talk/getTalkById', this.$route.query.talkId)
      .then(({data}) => {
        this.talk = data;
        if (data.imgList) {
          data.imgList.forEach(item => {
            this.uploadList.push({ url: item });
          });
        }
      })
    }
  },
  data: function() {
    return {
      emojiList: EmojiList,
      talk: {
        id: null,
        content: "",
        isTop: 0,
        status: 1,
        images: null
      },
      statusList: [
        { status: 1, desc: "公开" },
        { status: 2, desc: "私密" }
      ],
      uploadList: []  // 上传图片列表
    };
  },
  methods: {
    handleCommand(command) {
      this.talk.status = command;
    },
    addEmoji(key, value) {
      this.$refs.editor.addText(
        "<img src= '" +
        value +
        "' width='24'height='24' alt=" +
        key +
        " style='margin: 0 1px;vertical-align: text-bottom'/>"
      );
    },
    handleRemove(file) {
      this.uploadList.forEach((item, index) => {
        if (item.url == file.url) {
          this.uploadList.splice(index, 1);
        }
      });
    },
    // 上传文件之后回调函数
    upload(response) {
      if (response.code !== 20000){
        return  this.$notify.error(response.message);
      }
      this.uploadList.push({ url: response.data });
    },
    // 上传图片前操作
    beforeUpload(file) {
      console.log('上传说说图片:');
      console.log(file);
      return new Promise(resolve => {
        // 获取文件允许的最大的大小
        let fileMaxSize = this.$global.UPLOAD_FILE_MAX_SIZE;
        // 上传文件的实际大小
        let updateFileSize = file.size/1024/1024;
        if (updateFileSize < fileMaxSize) {
          resolve();
        }
        console.log('图片太大，压缩图片:');
        // 压缩到200KB,这里的200就是要压缩的大小,可自定义
        imageConversion
          .compressAccurately(file, this.$global.UPLOAD_FILE_SIZE)
          .then(res => {
            console.log('压缩封面后返回结果');
            console.log(res);
            resolve(res);
          });
      });
    },
    // 保存或修改说说
    saveOrUpdateTalk() {
      if (this.talk.content.trim() === "") {
        this.$message.error("说说内容不能为空");
        return false;
      }
      // 转换图片
      if (this.uploadList.length > 0) {
        var imgList = [];
        this.uploadList.forEach(item => {
          imgList.push(item.url);
        });
        this.talk.images = JSON.stringify(imgList);
      }
      this.$store.dispatch('talk/saveOrUpdateTalk',this.talk)
      .then(data => {
        if (data.flag) {
          this.$refs.editor.clear();
          this.uploadList = [];
          this.$notify.success({
            title: "发布成功",
            message: data.message
          });
        } else {
          this.$notify.error({
            title: "发布失败",
            message: data.message
          });
        }
      })
    }
  },
  computed: {
    dropdownTitle() {
      var desc = "";
      this.statusList.forEach(item => {
        if (item.status == this.talk.status) {
          desc = item.desc;
        }
      });
      return desc;
    }
  }
};
</script>

<style scoped>
.talk-container {
  margin-top: 40px;
}
.editor-wrapper {
  min-height: 150px;
}
.operation-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}
.operation-btn {
  cursor: pointer;
  color: #838383;
  font-size: 20px;
  margin-right: 12px;
}
.talk-status {
  cursor: pointer;
  font-size: 12px;
  color: #999;
}
.emoji {
  user-select: none;
  margin: 0.25rem;
  display: inline-block;
  vertical-align: middle;
}
.emoji-item {
  cursor: pointer;
  display: inline-block;
}
.emoji-item:hover {
  transition: all 0.2s;
  border-radius: 0.25rem;
  background: #dddddd;
}
.left-wrapper {
  display: flex;
  width: 50%;
}
.talk-image-upload {
  margin-top: 8px;
}
</style>
