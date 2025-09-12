package jornadajava.spring_boot_trench.mapper;

import jornadajava.spring_boot_trench.domain.Serie;
import jornadajava.spring_boot_trench.request.SeriePostRequest;
import jornadajava.spring_boot_trench.request.SeriePutRequest;
import jornadajava.spring_boot_trench.response.SerieGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SerieMapper {

    SerieMapper MAPPER = Mappers.getMapper(SerieMapper.class);

    Serie toSerie (SeriePostRequest postRequest);

    SerieGetResponse toSerieGetResponse (Serie serie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void SerieToUpdate (SeriePutRequest dto, @MappingTarget Serie serie);


}
