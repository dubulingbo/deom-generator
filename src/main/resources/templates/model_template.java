package #{packageDir};

import java.io.Serializable;
#{importZone}

/**
 * #{remark}实体类
 * @author #{user}
 * @since #{curTime}
 * i believe i can i do
 */
public class #{className} implements Serializable {
//    private static final long serialVersionUID = #{serialNumber}L;
#{propertyZone}

#{getAndSetMethod}

#{toStringMethod}
}