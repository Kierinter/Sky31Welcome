package com.sky31.controller;

import com.alibaba.fastjson2.JSON;
import com.sky31.domain.Comment;
import com.sky31.domain.DiscussPost;
import com.sky31.domain.Event;
import com.sky31.event.EventProducer;
import com.sky31.service.CommentService;
import com.sky31.service.DiscussPostService;
import com.sky31.utils.Constant;
import com.sky31.utils.HostHolder;
import com.sky31.utils.md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 21:31
 */
@RestController
@ResponseBody
@RequestMapping("/comment")
public class CommentController implements Constant {
    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;
    @Autowired
    HostHolder hostHolder;

    @Autowired
    DiscussPostService discussPostService;

    @RequestMapping(value = "/add/{discussPostId}",method = RequestMethod.POST)
    public Object addComment(@PathVariable("discussPostId")int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        comment.setMilsTime(System.currentTimeMillis());
        commentService.addComment(comment);
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId",discussPostId);
        if (comment.getEntityType()==ENTITY_TYPE_POST){
            DiscussPost post = discussPostService.selectDiscussPostById(comment.getEntityId());
            event.setEntityUserId(post.getUserId());
        }else if (comment.getEntityType()==ENTITY_TYPE_COMMENT){
            Comment target=commentService.selectCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);
//        return "redirect:/discuss/detail/"+discussPostId;
        if (comment.getEntityType()==ENTITY_TYPE_POST){
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
        }
        return md5Util.getJSONString(0,"发送评论成功");
    }
}
