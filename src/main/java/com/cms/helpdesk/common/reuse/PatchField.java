package com.cms.helpdesk.common.reuse;

import java.lang.reflect.Field;

public class PatchField<T> {

    public T fusion(T existing, T req) {
        Field[] fieldUsers = req.getClass().getDeclaredFields();
        for (Field field : fieldUsers) {
            field.setAccessible(true);
            try {
                Object value = field.get(req);
                if (value != null) {
                    Field userField = existing.getClass().getDeclaredField(field.getName());
                    userField.setAccessible(true);
                    userField.set(req, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return existing;
    }

}
