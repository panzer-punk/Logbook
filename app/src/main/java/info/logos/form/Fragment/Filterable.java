package info.logos.form.Fragment;

import info.logos.form.Network.Objects.Category;

public interface Filterable {
    void applyFilter(Category category);
    Category getCategory();
}
