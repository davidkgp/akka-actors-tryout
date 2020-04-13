package com.test.akka.messages.impl;

import com.test.akka.messages.ICommand;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public final class WordCountResponse implements ICommand {
    private final int count;
}
