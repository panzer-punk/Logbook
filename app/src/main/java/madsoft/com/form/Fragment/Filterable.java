package madsoft.com.form.Fragment;

import madsoft.com.form.Network.Objects.Category;

public interface Filterable {
    void applyFilter(Category category);
    Category getCategory();
}
