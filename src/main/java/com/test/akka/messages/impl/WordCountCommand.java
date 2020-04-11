package com.test.akka.messages.impl;

import akka.actor.typed.ActorRef;
import com.test.akka.messages.ICommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class WordCountCommand implements ICommand {
    private final String line;
    private final ActorRef<ICommand> sender;
}
