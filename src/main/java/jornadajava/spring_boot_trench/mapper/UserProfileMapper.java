package jornadajava.spring_boot_trench.mapper;

import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.domain.UserProfile;
import jornadajava.spring_boot_trench.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

   List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> userProfiles);

   UserProfileUserGetResponse toUserProfileUserGetResponse(User users);

}
