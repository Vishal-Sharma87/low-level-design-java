package com.vishal.lld.solid.dip.good.interfaces;

public interface Database {
    int save(Object order);
    Object findById(int id);
} 