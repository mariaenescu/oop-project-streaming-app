package app.audio.Collections;

import java.util.Objects;

public class Merch {
    private String name;
    private String description;
    private Integer price;

    public Merch(final String name, final String description, final Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final Integer getPrice() {
        return price;
    }

    @Override
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Merch merch)) {
            return false;
        }
        return Objects.equals(name, merch.name);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(name);
    }
}
