package jornadajava.spring_boot_trench.annotation;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface EncodeMapping {
}
//ElementType.TYPE: Pode colar em cima de uma Classe ou Interface
//ElementType.METHOD: Pode colar em cima de um Método.
//O compilador Java vai te impedir de colar isso em lugares errados (como em cima de uma variável ou de um parâmetro),
//evitando erros bobos.

//@Retention(RetentionPolicy.CLASS)
//O QUÊ: Define QUANTO TEMPO o adesivo fica colado.
//CLASS (O que esta sendo usado): O adesivo fica grudado no arquivo compilado (.class),
//mas a aplicação rodando (JVM) não precisa ver ele.
//O MapStruct trabalha durante a compilação (gerando código). Ele lê o arquivo .class ou o código fonte.
//Ele não precisa que essa anotação exista quando o site estiver no ar,
//então CLASS é a medida perfeita: dura o suficiente pro MapStruct ler, mas não gasta memória à toa depois.
