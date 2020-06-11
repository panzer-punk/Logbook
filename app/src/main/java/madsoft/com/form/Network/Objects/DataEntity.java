package madsoft.com.form.Network.Objects;

import java.io.Serializable;

import androidx.annotation.Nullable;

public interface DataEntity extends Serializable {

    Integer getId();
    @Nullable
    String getModified();

    String getTitleS();
    String getUrl();
}
