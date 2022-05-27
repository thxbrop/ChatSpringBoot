package com.thxbrop.springboot.controller;

import com.thxbrop.springboot.Result;
import com.thxbrop.springboot.ServerException;
import com.thxbrop.springboot.entity.Conversation;
import com.thxbrop.springboot.entity.Invite;
import com.thxbrop.springboot.entity.Member;
import com.thxbrop.springboot.entity.Message;
import com.thxbrop.springboot.repository.*;
import com.thxbrop.springboot.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ConversationController {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final InviteRepository inviteRepository;

    private final UserRepository userRepository;

    @Autowired
    public ConversationController(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            MemberRepository memberRepository,
            InviteRepository inviteRepository,
            UserRepository userRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
        this.inviteRepository = inviteRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/con/{id}")
    public Result<Conversation> getById(@PathVariable("id") int id) {
        return new Result<>(conversationRepository.findById(id).orElse(null));
    }

    @GetMapping("/con/members/{conId}")
    public Result<List<Integer>> getMembersByConId(@PathVariable("conId") int conId) {
        List<Integer> members = (StreamUtils.of(memberRepository.findAll())
                .filter(member -> member.getConId() == conId)
                .map(Member::getUserId)
                .collect(Collectors.toList()));
        return new Result<>(members);
    }

    @GetMapping("/con/member/{id}")
    public Result<Member> getMember(@PathVariable("id") int id) {
        return new Result<>(memberRepository.findById(id).orElse(null));
    }

    @GetMapping("/con/memberBlur")
    public Result<Member> getMember(@RequestParam int conId, @RequestParam int userId) {
        return new Result<>(
                StreamUtils.of(memberRepository.findAll())
                        .filter(member -> member.getConId() == conId && member.getUserId() == userId)
                        .findFirst().orElse(null)
        );
    }

    @GetMapping("/con/message/{conId}")
    public Result<List<Message>> getMessages(
            @PathVariable int conId,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<Message> list = StreamUtils.of(messageRepository.findAll())
                .filter(message -> message.getConId() == conId)
                .sorted((o1, o2) -> (int) (o1.getTimestamp() - o2.getTimestamp()))
                .limit(limit)
                .collect(Collectors.toList());
        return new Result<>(list);
    }

    @GetMapping("/con/save")
    public Result<Conversation> save(@RequestParam String name, @RequestParam int creatorId) {
        Conversation conversation = new Conversation(name, creatorId);
        Member member = new Member();
        Conversation save = conversationRepository.save(conversation);
        member.setConId(save.getId());
        member.setUserId(creatorId);
        memberRepository.save(member);
        return new Result<>(save);
    }

    @GetMapping("/con/send")
    public Result<Message> sendMessage(
            @RequestParam int conId,
            @RequestParam int userId,
            @RequestParam String content
    ) {
        Message message = new Message();
        message.setConId(conId);
        message.setUserId(userId);
        message.setContent(content);
        message.setTimestamp(System.currentTimeMillis());
        messageRepository.save(message);
        return new Result<>(message);
    }

    @GetMapping("/con/invite/{conId}")
    public Result<Invite> invite(
            @PathVariable("conId") int conId,
            @RequestParam int from,
            @RequestParam int to
    ) {
        Iterable<Member> all = memberRepository.findAll();
        boolean isRoomExist = StreamUtils.of(conversationRepository.findAll())
                .anyMatch(conversation -> conversation.getId() == conId);
        if (!isRoomExist) {
            return ServerException.CONVERSATION_NOT_EXIST.asResult();
        }
        boolean isFromUserExist = StreamUtils.of(userRepository.findAll())
                .anyMatch(user -> user.getId() == from);
        boolean isToUserExist = StreamUtils.of(userRepository.findAll())
                .anyMatch(user -> user.getId() == to);
        if (!isFromUserExist) {
            return ServerException.FROM_USER_NOT_EXIST.asResult();
        }
        if (!isToUserExist) {
            return ServerException.TO_USER_NOT_EXIST.asResult();
        }
        boolean isFromInRoom = StreamUtils.of(all)
                .anyMatch(member -> member.getUserId() == from);
        if (!isFromInRoom) {
            return ServerException.PERMISSION_DENIED.asResult();
        }
        boolean isToInRoom = StreamUtils.of(all)
                .anyMatch(member -> member.getUserId() == to);
        if (isToInRoom) {
            return ServerException.MEMBER_EXIST.asResult();
        }
        boolean isEffectiveInviteExist = StreamUtils.of(inviteRepository.findAll())
                .anyMatch(invite -> invite.getFromId() == from && invite.getToId() == to && invite.getState() == Invite.STATE_PENDING);
        if (isEffectiveInviteExist) {
            return ServerException.INVITE_EXIST.asResult();
        }
        Invite invite = new Invite();
        invite.setConId(conId);
        invite.setFromId(from);
        invite.setToId(to);
        inviteRepository.save(invite);
        return new Result<>(invite);
    }
}
