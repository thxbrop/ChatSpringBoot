package com.thxbrop.springboot.controller;

import com.thxbrop.springboot.Result;
import com.thxbrop.springboot.ServerException;
import com.thxbrop.springboot.entity.Invite;
import com.thxbrop.springboot.entity.Member;
import com.thxbrop.springboot.repository.InviteRepository;
import com.thxbrop.springboot.repository.MemberRepository;
import com.thxbrop.springboot.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class InviteController {
    private final InviteRepository inviteRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public InviteController(InviteRepository inviteRepository, MemberRepository memberRepository) {
        this.inviteRepository = inviteRepository;
        this.memberRepository = memberRepository;
    }

    private Result<Invite> action(int id, int state) {
        Invite invite = inviteRepository.findById(id).orElse(null);
        if (invite == null) {
            return ServerException.INVITE_NOT_EXIST.asResult();
        }
        if (invite.isApproved()) {
            return ServerException.INVITE_APPROVED.asResult();
        }
        if (invite.isRefused()) {
            return ServerException.INVITE_REFUSED.asResult();
        }
        inviteRepository.updateState(id, state);
        invite.setState(state);
        return new Result<>(invite);
    }

    @GetMapping("/invite/approve/{id}")
    public Result<Invite> approve(@PathVariable int id) {
        inviteRepository.findById(id).ifPresent(invite -> {
            Member member = new Member();
            member.setUserId(invite.getToId());
            member.setConId(invite.getConId());
            memberRepository.save(member);
        });
        return action(id, Invite.STATE_APPROVED);
    }

    @GetMapping("/invite/refuse/{id}")
    public Result<Invite> refuse(@PathVariable int id) {
        return action(id, Invite.STATE_REFUSED);
    }

    @GetMapping("/invite/out/{userId}")
    public Result<List<Invite>> outInvites(@PathVariable int userId) {
        List<Invite> list = StreamUtils.of(inviteRepository.findAll())
                .filter(invite -> invite.getFromId() == userId)
                .collect(Collectors.toList());
        return new Result<>(list);
    }

    @GetMapping("/invite/in/{userId}")
    public Result<List<Invite>> inInvites(@PathVariable int userId) {
        List<Invite> list = StreamUtils.of(inviteRepository.findAll())
                .filter(invite -> invite.getToId() == userId)
                .collect(Collectors.toList());
        return new Result<>(list);
    }
}