# TrackNGO
## Motivación

Existen numerosas organizaciones sin fines de lucro que realizan actividades con el fin de ayudar a los más necesitados. 
Muchas veces, son voluntarios quienes se ofrecen a efectuar el trabajo de tener los datos de quienes ayudan, ya sea 
económica o materialmente; de aquellos que son los beneficiarios, sean instituciones o individuos; de los materiales y 
recursos, de los gastos e ingresos o de los itinerarios.

En muchas de estas organizaciones estos datos se tienen en un archivo Excel, escrito a mano o guardado en documentos de 
texto plano, siendo estos fáciles de perderse registros, de generar duplicados o sufrir la falta de actualizaciones.

Estas organizaciones pueden tener distintos enfoques: pueden apuntar hacia el lado educativo, proveyendo clases de 
apoyo, libros y/o tutorías; económico, aportando fondos para realizar las operaciones que los receptores realizan; 
pueden enfocarse por el lado alimentario, donando alimentos a comedores; en construcción, aportando mano de obra; 
medicina, ofreciendo servicios en el área de salud; entre muchos otros enfoques distintos.

Con el fin de facilitarles el trabajo a los voluntarios quienes trabajan en estas organizaciones, se idea TrackNGO como 
una aplicación que pueda automatizar procesos y hacer más fácil el seguimiento de las organizaciones hacia los 
beneficiarios, pudiéndose identificar qué o quiénes son los aquellos quienes necesitan la ayuda más urgentemente o a 
quiénes hace mucho no se les dio una mano.

## Funcionalidades

El objetivo del desarrollo es, entonces, contar con una aplicación que pueda realizar las siguientes gestiones, que son 
esenciales para los usuarios para quienes se idea:
1. **ABM de usuarios**: se debe tener administradores del sistema quienes sean responsables de dar de alta, baja o modificar los registros de los usuarios.
2. **Persistencia de los registros**: se quiere que, dado una situación como el recibo de un pago o donación, se pueda persistir un registro de ello en la aplicación.
3. **Validaciones y evitar fraudes**: se debe validar la existencia de los usuarios que se dan de alta, los números de tarjetas al recibir pagos, suspensiones previas, etc. al momento de realizar gestiones.
4. **Régimen de suspensiones y rehabilitaciones**: se debe poder suspender a un colaborador, voluntario o beneficiario si se detecta alguna irregularidad y poder rehabilitarlo de comprobarse que la irregularidad era inexistente.
5. **Notificaciones**: se debe poder notificar por vía de correos electrónicos los eventos a los voluntarios.
