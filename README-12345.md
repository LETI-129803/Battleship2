# Qodana — Tarefa 2A

## 1. Objetivo

Configurar o Qodana para identificar automaticamente um code smell no projeto BattleShip2, integrando a análise estática no processo de CI/CD através do GitHub Actions.

## 2. Code Smell selecionado

**Suspicious Indentation After Control Statement Without Braces**

Este code smell ocorre quando uma instrução de controlo, como `if`, `for` ou `while`, não utiliza chavetas (`{}`), mas a indentação da instrução seguinte sugere incorretamente que esta pertence ao bloco de controlo.

Esta situação pode induzir o programador em erro, dificultar a leitura do código e originar erros lógicos.

## 3. Inspeção Qodana

A inspeção selecionada apresenta as seguintes características:

| Propriedade | Valor |
|---|---|
| Inspection ID | `SuspiciousIndentAfterControlStatement` |
| Nome | Suspicious indentation after control statement without braces |
| Categoria | Probable Bugs |
| Severidade | High (WARNING) |

A inspeção utiliza análise estática da estrutura e indentação do código. Não depende de limiares de métricas como LOC ou Cyclomatic Complexity.

## 4. Ficheiros de configuração

Foram criados os seguintes ficheiros:

- `QodanaProfile.xml`: ativa a inspeção selecionada com severidade WARNING.
- `qodana.yaml`: define o linter `qodana-jvm-community` e indica o caminho para o perfil personalizado.
- `.github/workflows/qodana.yml`: configura a execução automática da análise através do GitHub Actions.

### Configuração do perfil

O perfil personalizado ativa a inspeção `SuspiciousIndentAfterControlStatement`.

### Configuração qodana.yaml

```yaml
version: "1.0"

linter: qodana-jvm-community

profile:
  path: QodanaProfile.xml
```

## 5. Integração com GitHub Actions

Foi configurado um workflow para executar a análise Qodana automaticamente nos seguintes eventos:

- Push para a branch `main`;
- Criação ou atualização de Pull Requests;
- Execução manual através de `workflow_dispatch`.

A branch `main` encontra-se protegida através de regras de proteção e da exigência de verificações automáticas antes do merge, configuração partilhada pelo grupo.

## 6. Validação da inspeção

Para testar o funcionamento da inspeção, foi introduzida intencionalmente uma situação de indentação suspeita no ficheiro `Ship.java`.

### Primeira execução

O relatório do Qodana apresentou:

- 183 problemas detetados no total;
- 2 ocorrências da inspeção `Suspicious indentation after control statement without braces`.

### Segunda execução

Após a correção do código, através da utilização de chavetas e do ajuste da indentação, foi executada novamente a análise.

O relatório apresentou:

- 10 problemas detetados no total;
- 0 ocorrências da inspeção selecionada.

### Resultado

A comparação dos dois relatórios demonstra que a inspeção identificou corretamente o problema introduzido e deixou de o reportar após a sua correção.

## 7. Conclusão

A implementação permitiu configurar e integrar o Qodana no GitHub Actions, utilizando um perfil personalizado para identificar o code smell selecionado.

O teste realizado confirmou o funcionamento da inspeção, demonstrando a deteção do problema e a sua eliminação após a correção do código.