package jornadajava.spring_boot_trench.mapper;

import jornadajava.spring_boot_trench.domain.Filme;
import jornadajava.spring_boot_trench.request.FilmePostRequest;
import jornadajava.spring_boot_trench.request.FilmePutRequest;
import jornadajava.spring_boot_trench.response.FilmeGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FilmeMapper {

    FilmeMapper FILME_MAPPER = Mappers.getMapper(FilmeMapper.class);

    Filme toFilme(FilmePostRequest postRequest);

    FilmeGetResponse toFilmeGetResponse(Filme filme);

    @Mapping(target = "id", ignore = true)
    void FilmeToUpdate (FilmePutRequest dto, @MappingTarget Filme filme);
}
