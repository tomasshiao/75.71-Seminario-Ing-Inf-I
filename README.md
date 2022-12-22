# TrackNGO
## Motivación

Existen numerosas organizaciones sin fines de lucro que realizan actividades con el fin de ayudar a los más necesitados. Muchas veces, son voluntarios quienes se ofrecen a realizar el trabajo de tener los datos de quienes ayudan, ya sea económica o materialmente; de aquellos que son los beneficiarios, sean instituciones o individuos; de los materiales y recursos, de los gastos e ingresos o de los itinerarios.

En muchas de estas organizaciones estos datos se tienen en un archivo Excel, escrito a mano o guardado en documentos de texto plano, siendo estos fácil de perderse registros, de generar duplicados o sufrir la falta de actualizaciones.

Estas organizaciones pueden tener distintos enfoques: pueden apuntar hacia el lado educativo, proveyendo clases de apoyo, libros y/o tutorías; económico, aportando fondos para realizar las operaciones que los receptores realizan; pueden enfocarse por el lado alimentario, donando alimentos a comedores; en construcción, aportando mano de obra; medicina, ofreciendo servicios en el área de salud; entre muchos otros enfoques distintos.

Con el fin de facilitarles el trabajo a los voluntarios quienes trabajan en estas organizaciones, se idea TrackNGO como una aplicación que pueda automatizar procesos y hacer más fácil el seguimiento de las organizaciones hacia los beneficiarios, pudiéndose identificar qué o quiénes son los aquellos quienes necesitan la ayuda más urgentemente o a quiénes hace mucho no se les dio una mano.

Para lograr este fin, esta organización poseerá aportantes, ya sea de bienes materiales, comestibles, o incluso monetarios. A estos aportantes se los llamará “Amigos”, los cuales pueden ser personas físicas o jurídicas, tanto argentinas como extranjeras. Estos deberán pasar por un proceso de autenticación para poder unirse a la organización como Amigos.

Los Amigos son los que realizarán las “Donaciones”. Estas se clasificarán según la frecuencia en la que se hagan, ya sean únicas, semanales, quincenales, mensuales o anuales, y según el tipo de donación que sea, ya sea monetaria, de bienes materiales o de bienes comestibles, ya sean perecederos o no.

A su vez, la aplicación poseerá receptores de las donaciones, a los cuales se los llamará “Beneficiarios”. Estos, al igual que los Amigos, deberán pasar por un proceso de autenticación. Este mismo será más riguroso que el anterior, puesto que se puede tratar un caso de fraude, e incluso no será una validación que se hará en una primera ocasión nada más, sino que será hecha de manera periódica.  Para los casos positivos, la aplicación contará con una “BlackList”, la cual bloqueará a dichos Beneficiarios de forma provisoria, y luego de realizar un análisis más profundo a la denuncia del posible fraude, se eliminará de la Blacklist, realizándole una “Reconexión” al beneficiario, o se agregara a la Blacklist permanentemente, realizándole una “Suspension” al Beneficiario.

Como beneficio por parte de la organización, los Amigos podrán acceder a los “Eventos” organizados por la misma. Estos pueden ser actos de donaciones o caridades, eventos exclusivos para los Amigos, como por ejemplo comidas o salidas, o también Eventos festivos, por ejemplo, para las fechas de Navidad y Año Nuevo.

## Funcionalidades

El objetivo del desarrollo es, entonces, contar con una aplicación que pueda realizar las siguientes gestiones, que son 
esenciales para los usuarios para quienes se idea:
1. **ABM de usuarios**: se debe tener administradores del sistema quienes sean responsables de dar de alta, baja o modificar los registros de los usuarios.
2. **Persistencia de los registros**: se quiere que, dado una situación como el recibo de un pago o donación, se pueda persistir un registro de ello en la aplicación.
3. **Validaciones y evitar fraudes**: se debe validar la existencia de los usuarios que se dan de alta, los números de tarjetas al recibir pagos, suspensiones previas, etc. al momento de realizar gestiones.
4. **Régimen de suspensiones y rehabilitaciones**: se debe poder suspender a un colaborador, voluntario o beneficiario si se detecta alguna irregularidad y poder rehabilitarlo de comprobarse que la irregularidad era inexistente.
5. **Notificaciones**: se debe poder notificar por vía de correos electrónicos los eventos a los voluntarios.
