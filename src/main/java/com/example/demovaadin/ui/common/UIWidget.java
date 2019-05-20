package com.example.demovaadin.ui.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.server.Command;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public interface UIWidget {
    default public void access(Command command) {
        ((Component) this).getUI().ifPresent(ui -> ui.access(command));
    }

    default void autoSetCssClassNames() {
        if (this instanceof HasStyle){
            ((HasStyle) this).addClassName(this.getClass().getSimpleName());
            ((HasStyle) this).addClassName("JavaClass");
            ((HasStyle) this).addClassName("element");
        }
        for (Field f : getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                Object o = f.get(this);
                if (o instanceof HasStyle) {
                    List<String> l = Arrays.asList(f.getName().split("(?<=[a-z,0-9])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"));
                    l.forEach(s-> {
                        ((HasStyle) o).addClassName(s.toLowerCase());
                        ((HasStyle) o).addClassName("element");
                        }
                    );
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
