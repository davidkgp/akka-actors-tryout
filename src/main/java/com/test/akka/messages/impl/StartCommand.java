package com.test.akka.messages.impl;

import com.test.akka.messages.ICommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
@Getter
public final class StartCommand implements ICommand {
    private final File fileToBeCounted;

}
