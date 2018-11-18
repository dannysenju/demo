INSERT INTO usuario (idusuario, nombre_completo, username, password, rol, activo, email) VALUES (1, 'Super Admin', 'ADMIN', 'ba71596f5265b217fb889dc2f71693ea796fb590788ac4644c39db67ba06bbc13b7440b1a01c6a500f3f5868ba1430cc2a4a4a91c4a9d986b7c90634a0993877', 'ADMIN', 1, 'dannysenju@gmail.com');
INSERT INTO usuario (idusuario, nombre_completo, username, password, rol, activo, email) VALUES (2, 'Usuario de Test', 'TEST', 'ba71596f5265b217fb889dc2f71693ea796fb590788ac4644c39db67ba06bbc13b7440b1a01c6a500f3f5868ba1430cc2a4a4a91c4a9d986b7c90634a0993877', 'TEST', 1, 'dannysenju@gmail.com');

INSERT INTO modulo (idmodulo, nombre, descripcion, activo, url, icono) VALUES (1, 'compras', 'Información de compras', 1, '/views/compras/compras.xhtml', 'fe fe-shopping-cart');
INSERT INTO modulo (idmodulo, nombre, descripcion, activo, url, icono) VALUES (2, 'ventas', 'Información de ventas', 1, '/views/ventas/ventas.xhtml', 'fe fe-globe');

INSERT INTO modulo_usuario (id_modulo, id_usuario) VALUES (1, 1);
INSERT INTO modulo_usuario (id_modulo, id_usuario) VALUES (2, 1);
INSERT INTO modulo_usuario (id_modulo, id_usuario) VALUES (1, 2);
INSERT INTO modulo_usuario (id_modulo, id_usuario) VALUES (2, 2);






