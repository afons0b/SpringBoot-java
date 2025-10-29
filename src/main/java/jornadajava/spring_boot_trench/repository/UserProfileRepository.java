package jornadajava.spring_boot_trench.repository;

import jornadajava.spring_boot_trench.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    //por padrao, as anotaçoes ManyToOne sao do tipo EAGER nas buscas de um user e profile na tabela user_profile
    //consequentemente gerando querys desnecessarias, o problema de N + 1, ele faz uma query pra buscar todos
    //os user profile e uma query pra cada profile e pra cada user
    //entao se tivesse 100 profiles e 100 users, seria uma busca pra trazer todos os user_profile
    // e mais 100 buscas por profile e 100 por user, totalizando 201 querys
    //a anotação @EntityGraph resolve esse problema
    //em uma viagem so ela faz essa busca por perfis e usuarios em query so usando JOIN no banco
    //basta colocar as classes de relacionamento,
    //é uma forma de dizer quais vao ser usadas para esse join
    @EntityGraph(attributePaths = {"user", "profile"})
    List<UserProfile> findAll();

    //aqui estou fazendo uma
    @EntityGraph(attributePaths = {"user"})
    List<UserProfile> findByProfileId(Long id);
}
