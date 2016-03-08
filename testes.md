### Dificuldades

 - Rastrear as versões que estão sendo publicadas em desenvolvimento/testes/produção
 - Rastrear evoluções e bug fixes
 - Bancos de dados dropados em testes

#### Procedimentos de deploy

 - Melhorar rastreabilidade da versão publicada
    - Deploy sempre gera baseline com um número de versão (temos permissão pra criar baseline?)
    - Manter ambiente de desenvolvimento e testes sincronizados sempre que possível
 - Dificuldade em saber o que consta na versão publicada
    - Escrever notas de liberação a cada publicação

### Procedimentos de teste
 - Cenários de testes devem ter scripts para criação
 - Testar funcionalidades e bug fixes liberados e relados nas notas de liberação
 - Definir um ciclo de testes (a cada deploy?)
