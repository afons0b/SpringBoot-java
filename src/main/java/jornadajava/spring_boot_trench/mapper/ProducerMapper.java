package jornadajava.spring_boot_trench.mapper;

import jornadajava.spring_boot_trench.domain.Filme;
import jornadajava.spring_boot_trench.domain.Producer;
import jornadajava.spring_boot_trench.request.FilmePutRequest;
import jornadajava.spring_boot_trench.request.ProducerPostRequest;
import jornadajava.spring_boot_trench.response.ProducerGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);


    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000))")
    Producer toProducer (ProducerPostRequest postRequest);

    ProducerGetResponse toProducerGetResponse (Producer producer);

    List<ProducerGetResponse> toProduceGetResponseLIST(List<Producer> producers);

}
