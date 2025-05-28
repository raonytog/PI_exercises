# Lab Maven 01 - Automação de _Build_

Neste roteiro, vamos construir uma aplicação simples que lê dados disponíveis na Internet em formato CSV, reusando a biblioteca [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/). Em seguida, transformaremos essa primeira aplicação em uma API e criaremos um outro projeto que depende dela. A automação do _build_ e a gestão das dependências será feita pelo [Apache Maven](https://maven.apache.org/).

Ao longo do roteiro, há referências no estilo **(slide X)**, indicando que instruções de como fazer o que está sendo pedido encontram-se no slide número X da apresentação "Automação de Build com Maven", disponível no Classroom.

Este roteiro pode ser feito sozinho ou em grupo (estilo programação em pares). Caso seja feito em grupo, aproveite para compartilhar o código no GitHub e divida as tarefas entre os membros do grupo, enviando e obtendo o código do repositório compartilhado.

O resultado final deste roteiro de laboratório está disponível num [repositório no GitHub](https://github.com/vitorsouza/pi1-lifexprinter). Recomenda-se, no entanto, seguir o roteiro primeiro sem olhar o código pronto, deixando para compará-lo com o seu resultado posteriormente ou consultando pontualmente em caso de dúvida.



## Criação do projeto

1. Pense num `groupId` para seu projeto, lembrando que comumente se usa o endereço da sua organização na Web invertido. Por exemplos, os projetos do [LabES/UFES](https://labes.inf.ufes.br/) utilizam o `groupId` `br.ufes.inf.labes`;

2. Pense num `artifactId` para seu projeto com base num nome que queira dar a ele. O propósito do projeto será ler dados de expectativa de vida em diferentes países ao longo do tempo a partir de um arquivo CSV e imprimi-los ordenados;

3. Crie o projeto usando o arquétipo `maven-archetype-quickstart` **(slide 15)**. Lembre-se de ajustar a versão do Java no `pom.xml` **(slide 19)**;

4. Aproveite para fazer o controle de versões do seu projeto com o Git. O [repositório `github/gitignore` no GitHub](https://github.com/github/gitignore) possui exemplos de `.gitignore` interessantes para projetos Java e projetos gerenciados com Maven (você pode copiar o conteúdo de ambos para um `.gitignore` no seu projeto);

5. Teste o empacotamento **(slide 20)** e a execução **(slide 21)** do seu projeto. Se quiser, configure o _plugin_ JAR para indicar a classe principal do projeto **(slide 37)**;


## Implementação da leitura dos dados CSV

Aproveite para simular o desenvolvimento de um projeto de software real no Git. Faça o _commit_ inicial do que foi implementado no passo anterior e crie um ramo temporário para implementar a nova funcionalidade conforme explicação abaixo. Se preferir, antes do ramo temporário crie um ramo `dev` e deixe o ramo `main` apenas para os lançamentos (_releases_).

6. Obtenha os dados de expectativa de vida de uma [página do site Our World in Data](https://ourworldindata.org/grapher/female-and-male-life-expectancy-at-birth-in-years), clicando no botão de download abaixo do gráfico e selecionando a opção _Full data (CSV)_. Examine o arquivo para entender o formato;

7. Procure por `commons-csv` no [repositório central do Maven](https://mvnrepository.com/) e inclua a última versão desta biblioteca como uma dependência do seu projeto **(slide 38)**;

8. Altere seu programa (classe `App.java`) para ler o arquivo CSV, usando a API do Commons CSV, e imprimir os países com expectativa de vida (em 2020) na ordem decrescente (pode escolher dentre a expectativa de vida masculina ou feminina);

    > Veja no [guia do Commons CSV](https://commons.apache.org/proper/commons-csv/user-guide.html) alguns exemplos simples de processamento do CSV, particularmente nas seções [_Accessing column values by index_](https://commons.apache.org/proper/commons-csv/user-guide.html#Accessing_column_values_by_index) e [_Header auto detection_](https://commons.apache.org/proper/commons-csv/user-guide.html#Header_auto_detection).

9. Teste seu programa dentro de uma IDE para que o suporte Maven inclua a biblioteca Commons CSV na hora da execução. Como alternativa, você pode fazer o empacotamento do seu projeto com as dependências incluídas no JAR **(slide 42)** ou você pode usar o plugin `exec` do Maven da seguinte forma (substituir `br.ufes.inf.pi1.App` pelo nome completo da sua classe):

    ```sh
    mvn compile exec:java -Dexec.mainClass="br.ufes.inf.pi1.App"
    ```

    > Nota: o comando acima diz ao Maven para executar 2 coisas: a fase `compile` e o objetivo `exec:java` (afinal, para poder executar é preciso que esteja compilado). Você pode passar múltiplas fases/objetivos para o Maven que ele os executa em sequência. Por exemplo: `mvn clean package` limpa o projeto e faz o empacotamento do mesmo do zero.


## Transformação em API e publicação no repositório local

Caso esteja simulando o desenvolvimento de um projeto de software com Git, faça _commit_ da funcionalidade implementada anteriormente, mescle com o ramo `dev` (ou `main` caso não tenha feito o `dev`) e crie um novo ramo temporário para a nova funcionalidade descrita a seguir.

10. Modifique seu projeto para que ele ofereça os dados ordenados como uma API. Ou seja:

    1. Crie uma classe de domínio para representar a informação de expectativa de vida (masculina e feminina) de um país, num determinado ano (basicamente uma classe que represente uma linha do arquivo CSV);

    2. Crie uma classe que possua um método que, dado o nome do arquivo CSV, um ano e uma opção entre a masculina ou a feminina, retorne uma lista de instâncias da classe de domínio mencionada acima ordenada de forma decrescente pela expectativa de vida escolhida (masculina ou feminina);

    3. Para testar, faça o seu programa (método `App.main()`) usar a nova API para produzir o mesmo resultado que produzia anteriormente.

11. Disponibilize seu projeto em seu repositório local (`.m2/repository`), usando o comando `mvn install`;

12. Verifique onde o Maven colocou os artefatos relacionados ao seu projeto no seu repositório local. Veja o conteúdo do POM que foi disponibilizado no repositório;


## Utilização da API e dependências transitivas

Um novo projeto será criado nos passos a seguir. Fique à vontade para usar ou não o Git neste novo projeto.

13. Seguindo novamente os passos 1 a 5, crie um novo projeto que irá usar o seu primeiro projeto como uma dependência;

14. Adicione seu primeiro projeto como uma dependência no `pom.xml` **(slide 38)**, utilizando as informações de `groupId`, `artifactId` e `version` do `pom.xml` do seu primeiro projeto na configurações da `<dependency>` neste novo projeto;

    > Caso esteja usando o Visual Studio Code e tenha instalada a extensão [Maven for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-maven) (que faz parte do pacote de extensões [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)), abra a visão _MAVEN_ e veja abaixo do item _Dependencies_ que o Commons CSV é adicionado transitivamente como uma dependência do seu projeto.

15. Implemente novamente a funcionalidade do passo 8, porém desta vez usando a API construída no primeiro projeto ao invés de usar o Commons CSV diretamente;

16. Execute seu projeto para testar se funciona. Chame o professor para mostrar o resultado.

