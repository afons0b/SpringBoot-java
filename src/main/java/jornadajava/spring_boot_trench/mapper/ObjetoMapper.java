package jornadajava.spring_boot_trench.mapper;

import jornadajava.spring_boot_trench.domain.Objeto;
import jornadajava.spring_boot_trench.response.ObjetoGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ObjetoMapper {
    ObjetoMapper INSTANCE = Mappers.getMapper(ObjetoMapper.class);

    ObjetoGetResponse toObjetoGetresponse(Objeto objeto);

    List<ObjetoGetResponse> toOjetoGetResponseList(List<Objeto> objetos);
}
