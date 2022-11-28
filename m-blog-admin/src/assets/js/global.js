import moment from "moment";

export default{
  UPLOAD_FILE_MAX_SIZE: 1, // 上传文件最大大小，单位MB
  UPLOAD_FILE_SIZE: 200, // 压缩文件之后的大小,单位KB
  // 小文件(图标大小)
  UPLOAD_SMALL_FILE_SIZE: 50, // 压缩更小的格式
  // 默认头像路径
  DEFAULT_AVATAR_URL: 'http://www.static.mingzib.xyz/blogGetDefaultFile/defaultAvatar.jpg',
  // 默认文章封面路径
  DEFAULT_COVER_URL: 'http://www.static.mingzib.xyz/blogGetDefaultFile/deafaultArticleCover.png',

  // 前台路径
  // RECEPTION_URL: 'http://localhost:8081/',
  RECEPTION_URL: 'http://www.mingzib.xyz/',

  // 时间格式化
  dateFormat(row, column) {
    var date = row[column.property];
    if (date === undefined) {
      return "";
    }
    //修改时间格式 我要修改的是"YYYY-MM-DD"
    return moment(date).format("YYYY-MM-DD HH:mm");
  }

}
