# Mediapicker
 El examen esta realizado en un 75%, solo se completo el caso 1
 Elegi una arquitectura de tipo clean, coroutines para temas de asincronia, las partes mas citales del codigo estaod comentadas usando JavaDoc.

 * Para compilar es necesario descargar el sdk que esta especificado en el gradle, se recomienda desarrollo con Android 10.
 * Lo casos de uso funcionan correctamente y tiene su correspondiente unitest.
 * Tiene traduccion para el menu del tab
 * Se configuraron los favors
 * Diseño [linkDesign]
 * Se agrego un colllapsingToolbar para la parte del menu.
 * Arquitectura facil de expander
 * El apk de puede descargar de aqui [apk]

[linkDesign]: https://app.zeplin.io/project/5fc1ac1671b556055c1c3a36/screen/5fc1ac6ec45307bcccfda2d1

[apk]: https://github.com/ZetDeveloper/Mediapicker/blob/main/YFTC%20-%20DCJR.apk


#Arquitectura

![alt text](https://i.ibb.co/4ZC9GKK/Captura.png)

 * La vista contiene in view model(LiveData) que enviar ordenes para obtener los archivos
 * El view model consulta a la dependencia de casos de uso
 * El caso de uso consulta al repositorio que a su vez consulta a un util para hacer querys a os archivos.
 * Cuando el usecase devuelve respuesta el se hace un post del valos de los archivos para que el live data haga render de los item en el recycler view
 * Query usa un ContentResolver y cursores para obtener los archivos