package com.ming.m_blog.controller;

import com.ming.m_blog.dto.talk.TalkBackDTO;
import com.ming.m_blog.dto.talk.TalkDTO;
import com.ming.m_blog.enums.FilePathEnum;
import com.ming.m_blog.service.CommentService;
import com.ming.m_blog.service.TalkService;
import com.ming.m_blog.strategy.context.UploadFileContext;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.ResponseResult;
import com.ming.m_blog.vo.TalkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 说说模块
 */
@Api(tags = "说说模块")
@RestController
public class TalkController {

    @Autowired
    private TalkService talkService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UploadFileContext uploadFileContext;

    @ApiOperation("后台查看说说列表")
    @GetMapping("/admin/talks")
    public ResponseResult<PageResult<TalkBackDTO>> getTalks(QueryInfoVO queryInfoVO){
        return ResponseResult.ok(talkService.adminTalkList(queryInfoVO));
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("说说上传图片")
    @PostMapping("/talks/images")
    public ResponseResult<String> updateImage(MultipartFile file){
        return ResponseResult.ok(uploadFileContext.executeUploadStrategyMap(file, FilePathEnum.TALK.getPath()));
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("保存或修改说说")
    @PostMapping("/saveOrUpdate")
    public ResponseResult<?> saveOrUpdate(@Valid @RequestBody TalkVO talkVO){
        // 上传图片
        talkService.saveOrUpdateTalk(talkVO);
        return ResponseResult.ok();
    }

    @ApiOperation("通过id查询说说")
    @GetMapping("/admin/talks/{talkId}")
    public ResponseResult<TalkBackDTO> getBackTalkById(@PathVariable("talkId") Integer talkId){
        return ResponseResult.ok(talkService.getTalkBackById(talkId));
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("通过id删除说说")
    @DeleteMapping("/admin/talks")
    public ResponseResult<?> delTalkById(@RequestBody List<Integer> talkIdList){
        talkService.delTalkByIds(talkIdList);
        return ResponseResult.ok();
    }

    @ApiOperation("查看首页说说")
    @GetMapping("/home/talks")
    public ResponseResult<List<String>> getHomeTalks(){
        return ResponseResult.ok(talkService.getHomeTalks());
    }

    @ApiOperation("查看说说列表")
    @GetMapping("/talks")
    public ResponseResult<PageResult<TalkDTO>> getPTalks(){
        // 前台查询说说列表
        return ResponseResult.ok(talkService.getTalkList());
    }

    @ApiOperation("通过id查询前台说说")
    @GetMapping("/talks/{talkId}")
    public ResponseResult<TalkDTO> getTalkById(@PathVariable("talkId")Integer talkId){
        return ResponseResult.ok(talkService.getTalkById(talkId));
    }

    @ApiOperation("说说点赞")
    @PostMapping("/talk/{topicId}/like")
    public ResponseResult<?> talkLike(@PathVariable("topicId")Integer topicId){
        talkService.saveTalkLike(topicId);
        return ResponseResult.ok();
    }
}
