# SUPABOT

Bot para uso no chat da twitch que recuperar informações do site [speedrun.com](http://www.speedrun.com). Desenvolvido com java, utilizando [twitch4j](https://twitch4j.github.io/) para se comunicar com a [twitch](http://twitch.com), [spring boot](https://spring.io/projects/spring-boot) e [h2database](https://www.h2database.com/html/main.html) em persistir os dados.

## Instalação

Utilize o [maven](https://maven.apache.org/) para gerar o build

```bash
mv clean compile package -Pprod
```

## Uso

Para utilização do bot é necessário informar o token de acesso ao chat da twitch. Para isto é interessante criar uma conta nova para o bot e então gerar o token no site [twitch chat oath](https://twitchapps.com/tmi/). Além disso é obrigatório informar o nome da twitch que o bot irá se conectar, com base este nome o bot irá realizar as pesquisas pelas runs registradas no site [speedrun.com](http://speedrun.com)
```bash
java -jar supabot-1.0-all.jar chat-access-token=TWITCH_CHAT_TOKEN twitch-name=TWITCH_NAME
```

## Comandos do bot
### Comandos públicos
* !pb -> apresenta o pb do jogo definido como padrão, caso jogo não definido uma mensagem no chat irá pedir para usar o comando !setpbgame
* !randompb -> com base as runs, apresenta uma run aleatória no chat
* !top -> Melhores 3 tempos com base a run definida como padrão

### Comandos para moderadores
* !setpbgame [nome do jogo] -> define a run com base o nome do jogo informado, caso existam várias categorias de uma mesma run uma lista em chat será apresentada informando o número a ser escolhido. Neste momento deverá ser utilizado o comando !setpbbyid
* !setpbbyid [NÚMERO] -> informa a run definida pelo número dela
* !update -> força atualização dos dados locais com a [speedrun.com](http://www.speedrun.com)