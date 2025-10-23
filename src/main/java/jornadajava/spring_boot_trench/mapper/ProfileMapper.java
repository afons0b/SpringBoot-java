package jornadajava.spring_boot_trench.mapper;

import jornadajava.spring_boot_trench.domain.Profile;
import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.request.ProfilePostRequest;
import jornadajava.spring_boot_trench.response.ProfileGetResponse;
import jornadajava.spring_boot_trench.response.ProfilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    Profile toProfile (ProfilePostRequest postRequest);

    ProfileGetResponse toProfileGetResponse (Profile profile);

    ProfilePostResponse toProfilePostResponse(Profile profile);
}
