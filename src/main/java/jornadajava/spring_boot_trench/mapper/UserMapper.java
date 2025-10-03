package jornadajava.spring_boot_trench.mapper;

import jornadajava.spring_boot_trench.domain.User;
import jornadajava.spring_boot_trench.request.UserPostRequest;
import jornadajava.spring_boot_trench.request.UserPutRequest;
import jornadajava.spring_boot_trench.response.UserGetResponse;
import jornadajava.spring_boot_trench.response.UserPostResponse;
import jornadajava.spring_boot_trench.response.UserPutResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

//transformando o mapper em um bean para fazer injeção de dependencia
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    // por que estamos usando o mapper?
    // mapper serv para mapear atributos e poupar tempo
    // imagine se uma classe tem 10 atributos
    // e voce tenha que ficar fazendo set por set na hora de transformar para DTO
    // basta ensinar ele a transformar o que para o que


    @Mapping(target = "id", ignore = true)
    User toUser (UserPostRequest postRequest);

    UserGetResponse toUserGetResponse (User user);

    @Mapping(target = "id", ignore = true)
    void UserToUpdate (UserPutRequest dto, @MappingTarget User user);

    UserPostResponse toUserPostResponse(User user);

    UserPutResponse toUserPutResponse(User user);
}
