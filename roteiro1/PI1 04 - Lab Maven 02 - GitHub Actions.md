# Lab Maven 02 - GitHub Actions, Conventional Commits e Semantic Versioning

Neste roteiro, vamos conhecer uma ferramenta do GitHub que permite executar ações em resposta a determinadas situações, diretamente no GitHub. Essa ferramenta pode ser usada para implementar Integração Contínua ou Entrega Contínua (CI/CD - Continuous Integration / Continuous Deployment), dentre outras utilidades.

Também vamos conhecer duas propostas para organizar nosso trabalho. A primeira é uma especificação para dar um significado legível às mensagens de _commit_ do Git para humanos e máquinas, chamada _Conventional Commits_. A segunda é uma proposta para organizar os números de versão de modo a tentar escapar do "inferno das dependências", chamado _Semantic Versioning_.

O resultado final deste roteiro de laboratório está disponível num [repositório no GitHub](https://github.com/vitorsouza/pi1-printemp). Recomenda-se, no entanto, seguir o roteiro primeiro sem olhar o código pronto, deixando para compará-lo com o seu resultado posteriormente ou consultando pontualmente em caso de dúvida.


## Preparação

1. Verifique se o acesso ao [GitHub](https://github.com/) está configurado adequadamente (vide **Lab Git 01 - Chaves SSH**);

2. Leia [a página em português da proposta _Conventional Commits_](https://www.conventionalcommits.org/pt-br/v1.0.0/), utilize esta proposta para todos os _commits_ feitos ao longo deste roteiro;


## Criação do projeto e publicação no GitHub

3. Conforme já realizado no **Lab Maven 01 - Automação de _Build_**, crie um novo projeto com o Maven. Ao escolher o nome do artefato, considere que o objetivo deste projeto será acessar dados abertos de metereologia e imprimir a temperatura atual de uma cidade;

4. Inicialize um repositório Git na pasta do projeto recém-criado **(Git, slide 23)**, mas não faça nenhum _commit_ por enquanto;

5. Crie um repositório no GitHub para este projeto e adicione o GitHub como um repositório remoto em sua máquina local com o nome `origin` **(Git, slide 48)**;

6. Use o comando `git pull origin main` para trazer o ramo principal do repositório do GitHub para o repositório local. Alterne para o ramo `main` **(Git, slide 62)** e exclua o ramo `master` criado no repositório local **(Git, slide 69)**;

7. Faça commit dos arquivos criados pelo Maven (após os devidos ajustes, i.e.: versão do Java, inclusão de `.gitignore`) e sincronize com o GitHub. Lembre-se de seguir os padrões do _Conventional Commits_;


## Implementação do projeto

Ao longo da implementação, se quiser mais uma vez poderá praticar o Git e utilizar um modelo de _Git Flow_ (ex.: **Git, slide 77**), criando ramos temporários, fazendo o _merge_, etc.

8. Abra a URL https://api.open-meteo.com/v1/forecast?latitude=-20.27&longitude=-40.31&current_weather=true&forecast_days=1&timezone=America%2FSao_Paulo em seu navegador e veja que ela é uma API aberta que retorna dados metereológicos a partir dos parâmetros passados na própria URL. Observe também que o formato é JSON (_JavaScript Object Notation_, caso não conheça, leia mais na [documentação do JSON](https://www.json.org/json-pt.html));

9. Busque no [MVN Repository](https://mvnrepository.com/) os seguintes artefatos e adicione suas últimas versões estáveis como dependências em seu projeto **(Maven, slide 38)**:

    - `jfiglet` (grupo `com.github.lalyos`);
    - `httpclient5` (grupo `org.apache.httpcomponents.client5`);
    - `gson` (grupo `com.google.code.gson`).

10. A partir da [documentação do HttpClient](https://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html), altere seu programa para que se conecte na URL mencionada no passo 8 e imprima no console o conteúdo JSON retornado por ela. Teste e faça commit desta nova funcionalidade;

    > O exemplo da documentação utiliza `EntityUtils.consume()` para consumir a resposta, porém o método `EntityUtils.toString()` pode ser mais útil neste caso, retornando a resposta JSON em uma _string_ para imprimirmos na tela (e prosseguir com o próximo passo).

11. A partir da [API da classe JsonParser](https://javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/JsonParser.html), obtenha o valor de `temperature` dentro do objeto `current_weather` que faz parte do conteúdo JSON obtido no passo anterior. Teste e faça commit desta nova funcionalidade;

    > Observe que:
    > - O método `parseString()` retorna um `JsonElement`;
    > - Um `JsonElement` pode ser convertido para `JsonObject` com o método `getAsJsonObject()`;
    > - A partir de um `JsonObject`, é possível obter um outro objeto interno a ele com o método `getAsJsonObject()`, passando o nome do objeto como parâmetro;
    > - Por outro lado, o método `.get()` retorna um `JsonElement`, que possui métodos para avaliar valores literais como, por exemplo, `getAsDouble()` para retornar um valor `double`.

12. Por fim, a partir da [documentação do JFiglet](https://github.com/lalyos/jfiglet?tab=readme-ov-file#usage---code), imprima a temperatura (adicionando `°C`) como um _ASCII Banner_ (método `FigletFont.convertOneLine()`). Teste e faça commit desta nova funcionalidade. O resultado deve ser parecido com o seguinte:

    ```
      ____  _____ ___   __   ____
     |___ \|___ // _ \ /  \ / ___|
       __) | |_ \ (_) | () | |
      / __/ ___) \__, |\__/| |___
     |_____|____(_)/_/      \____|
    ```


## _Build_ na nuvem com GitHub Actions

13. Crie a pasta `.github/workflows` e, dentro dela, o arquivo `development.yml` com o seguinte conteúdo (foi usado `development` como ramo assumindo que foi criado um ramo com este nome em seu _Git Flow_, caso contrário troque para o ramo no qual você esteja fazendo os _commits_, ex.: `main`):

    ```yml
    # This workflow will build a Java project with Maven, and cache/restore any dependencies to improve the workflow execution time
    # For more information see: https://help.github.com/actions/language-and-framework-guides/building-and-testing-java-with-maven
    
    name: Java CI with Maven
    
    on:
      push:
        branches: [ development ]
      pull_request:
        branches: [ development ]
    
    jobs:
      build:
    
        runs-on: ubuntu-latest
    
        steps:
        - name: Checkout source code from repository
          uses: actions/checkout@v4
        - name: Set up virtual machine with JDK 21
          uses: actions/setup-java@v4
          with:
            java-version: '21'
            distribution: 'temurin'
            cache: maven
        - name: Build with Maven
          run: mvn -B package --file pom.xml
    ```

    > O arquivo acima é escrito numa linguagem chamada YAML (_YAML Ain't Markup Language_), uma linguagem para serialização de dados independente de linguagem de programação. Visite o [site do YAML](https://yaml.org/) para saber mais.
    >
    > O que o arquivo acima define:
    > - Um fluxo chamado `Java CI with Maven`;
    > - Que este fluxo deve ser executado sempre que houver um _push_ ou um _pull request_ no ramo `development`;
    > - Uma única tarefa (_job_) chamada `build` que compõe o fluxo;
    > - Que esta tarefa deve rodar num executor Linux usando a versão mais recente do Ubuntu, provido pelo GitHub;
    > - Três passos (_steps_) compõem a tarefa: (1) fazer o _checkout_ do código do repositório; (2) configurar um JDK versão 21 e o Maven (utilizando um cache para que o Maven não tenha que fazer download de tudo novamente a cada operação); e (3) empacotar a aplicação com o Maven.

14. Faça _commit_ e _push_ deste arquivo para o seu repositório GitHub (faça _push_ para o ramo indicado no arquivo, de modo a ativar o fluxo);

15. Na página do seu repositório no GitHub, clique em **Actions** e veja que o fluxo definido no arquivo encontra-se listado à esquerda, abaixo de **Workflows**. Além disso, à direita deve aparecer uma execução deste fluxo, associada à mensagem de _commit_ que foi feita;

16. Clique na mensagem de _commit_ para abrir o resultado da execução do fluxo. Veja que a tarefa `build` foi executada;

17. Clique na tarefa `build` para abrir seu resultado, expanda os diferentes passos da tarefa (note que há alguns que o GitHub incluiu, além dos 3 passos definidos no fluxo). Caso você tenha mantido a classe `AppTest` criada pelo Maven no projeto básico, repare que no passo `Build with Maven` os testes do seu projeto são executados, o que é parte de um fluxo de Integração Contínua;

    > Num projeto grande, nesta hora todos os testes já construídos até o momento seriam novamente executados automaticamente pelo processo de Integração Contínua (CI). Caso as mudanças feitas no último _commit_ tenham introduzido algum _bug_ que faça um destes testes falhar, isso seria detectado automaticamente, o que é um dos propósitos da CI.


## Versionamento Semântico e release com GitHub Actions

18. Leia a [página em português da proposta do _Semantic Versioning_](https://semver.org/lang/pt-BR/) para entender a proposta;

19. Crie um novo _workflow_ (arquivo `.yml` dentro de `.github/workflows`) para fazer um _release_ toda vez que um tag com formato `v*.*.*` for empurrado ao repositório:

    ```yml
    name: CI and release on main tags
    
    on:
      push:
        tags:
          - "v*.*.*"
      pull_request:
        tags:
          - "v*.*.*"

    permissions:
      contents: write
    
    jobs:
      build:
    
        runs-on: ubuntu-latest
    
        steps:
        - name: Checkout source code
          uses: actions/checkout@v4
        - name: Set up JDK 21
          uses: actions/setup-java@v4
          with:
            java-version: '21'
            distribution: 'temurin'
            cache: maven
        - name: Build with Maven
          run: mvn -B package --file pom.xml
        - name: Release
          uses: softprops/action-gh-release@v2
          if: startsWith(github.ref, 'refs/tags/')
          with:
            files: target/*.jar
    ```

20. Modifique a versão do seu artefato no `pom.xml` para `0.0.1` (ou alguma outra versão, seguindo a idea do Versionamento Semântico), faça _commit_ e _push_;

21. Crie uma _tag_ leve **(Git, slide 51)** chamada `v0.0.1` (use o mesmo número de versão do passo anterior) e envie a _tag_ ao repositório **(Git, slide 52)**;

22. Na seção **Actions** do seu repositório no GitHub, veja que o novo fluxo `CI and release on main tags` foi executado. Navegue até o resultado do passo `Release` e veja o que aconteceu;

23. Em seguida, adicione mais 2 passos ao final deste novo fluxo:

    ```yml
        - name: Copy target to release staging area
          run: mkdir staging && cp target/*.jar staging
        - name: Upload staged files to a package
          uses: actions/upload-artifact@v4
          with:
            name: Package
            path: staging
    ```

24. Incremente a versão de correção (_patch_) no `pom.xml` (ex.: `0.0.2`), faça _commit_ e _push_, crie uma nova _tag_ leve para esta nova versão e envie-a ao GitHub;

25. Veja no resultado da nova execução do fluxo a seção **Artifacts**;

26. Chame o professor para mostrar o resultado do lab.
