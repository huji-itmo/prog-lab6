package dataStructs.communication;

import commands.CommandData;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Request implements Serializable {
    CommandData commandData;

    List<Object> params;

    @Serial
    private static final long serialVersionUID=1L;
}
