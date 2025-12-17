package jornadajava.spring_boot_trench.response;

public record CepGetResponse(String cep, String state, String city, String neighborhood, String street, String service) {
}
