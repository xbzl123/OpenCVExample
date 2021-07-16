package com.example.opencvexample.equal;

import androidx.annotation.Nullable;

abstract class EqualClass extends Object {

    @Override
    public boolean equals(@Nullable Object obj) {
        Class<? extends EqualClass> aClass = this.getClass();
        return super.equals(obj);
    }
}
