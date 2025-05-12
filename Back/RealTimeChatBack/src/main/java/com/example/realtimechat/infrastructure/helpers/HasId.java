package com.example.realtimechat.infrastructure.helpers;

@FunctionalInterface
public interface HasId<Tid> {

    public Tid getId();

}
