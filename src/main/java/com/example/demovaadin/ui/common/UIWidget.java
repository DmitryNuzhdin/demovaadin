package com.example.demovaadin.ui.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.server.Command;

import java.lang.reflect.Field;

public interface UIWidget {
    default public void access(Command command) {
        ((Component) this).getUI().ifPresent(ui -> ui.access(command));
    }

    default void autoSetCssClassNames() {
        if (this instanceof HasStyle){
            ((HasStyle) this).addClassName(this.getClass().getSimpleName()+"-"+"this");
        }
        for (Field f : getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                Object o = f.get(this);
                if (o instanceof HasStyle)
                    ((HasStyle) f.get(this)).addClassName(this.getClass().getSimpleName()+"-"+ f.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
