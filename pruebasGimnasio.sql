insert into miembros (nombre, apellido, fecha_nacimiento,dni,telefono,correo,id_membresia) 
values ('Jorge','Jorge','2006-07-29','7439879J','976554885','Jorge@gmail.com',3);

select * from miembros;

select * from clases;

select * from miembro_clase;

insert into miembro_clase (id_miembro,id_clase) 
values (1,1);

select miembros.*, miembro_clase.id_clase from miembro_clase
join miembros on miembros.id_miembro=miembro_clase.id_miembro
join clases on clases.id_clase=miembro_clase.id_clase;