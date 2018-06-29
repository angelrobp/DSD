var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");

var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = {"html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

//Variables limite sensores
var termostato = 25;
var lumens = 2500;
var humedad = 20;

var TEMP_MAX = 40;
var TEMP_MIN = 0;
var LUZ_MIN = 1000;
var LUZ_MAX = 8000;
var TEMP_AC = 25;
var LUZ_INTERIOR = 4000;
var HUMEDAD_MIN = 10;
var TEMP_MIN_HUMEDAD = 25;

// Funciones auxiliares
function getValor(tipo) {
    if (tipo == "temp")
        return termostato;
    else if (tipo == "luz")
        return lumens;
    else if (tipo == "humedad")
        return humedad;
    else
        return "Error: get servidor";


}
;

function setValor(tipo, valor) {

	console.log("SET VALOR");
    if (tipo === "temp"){
      /*if(valor >= TEMP_MAX)
        termostato = TEMP_MAX;
      else if (valor <= TEMP_MIN)
        termostato = TEMP_MIN;
      else*/
        termostato = valor;
    }

    else if (tipo === "luz")
      /*if(valor >= LUZ_MAX)
        lumens = LUZ_MAX;
      else if (valor <= LUZ_MIN)
        lumens = LUZ_MIN;
      else*/
        lumens = valor;
    else if (tipo === "humedad")
      /*if(valor >= LUZ_MAX)
        lumens = LUZ_MAX;
      else if (valor <= LUZ_MIN)
        lumens = LUZ_MIN;
      else*/
        humedad = valor;

    else
        return "Error: set servidor";
        console.log("Actualizado el valor del sensor:" + tipo + " con " + valor);
}


/**
 * Devuelve un código con el estado del agente.
 * 0 = Ok
 * 1 = Temperatura elevada
 * 2 = Luz baja
 * 3 = Luz muy fuerte.
 * 4 = Temperatura baja
 * 5 = Humedad y temperatura bajas
 * @returns {Number} código de control del agente.
 */

function controlAgente() {
	console.log("Datos Actuales: "+ termostato + ", " + lumens + ", " + humedad);
    var mensaje = 0;

    if (termostato > TEMP_MAX) {
        mensaje = 1;
    }

    if (lumens <= LUZ_MIN) {
        mensaje = 2;
    }

    if (lumens >= LUZ_MAX) {
        mensaje = 3;
    }

    if (termostato < TEMP_MIN){
        mensaje = 4;
    }
	
	if (humedad < HUMEDAD_MIN && termostato <= TEMP_MIN_HUMEDAD){
	    mensaje = 5;
	}

    return mensaje;
}




//Creación servidor


var httpServer = http.createServer(
        function (request, response) {
            var uri = url.parse(request.url).pathname;
		
            if (uri == "/cliente") {
		uri = "/cliente.html";
		console.log("ME LLEGA1: "+ uri);
	    } else if (uri == "/sensor") {
		uri = "/sensor.html";
	    }

            var fname = path.join(process.cwd(), uri);
            fs.exists(fname, function (exists) {
                if (exists) {   // Redirigimos a cliente.html
                    fs.readFile(fname, function (err, data) {
                        if (!err) {
                            var extension = path.extname(fname).split(".")[1];
                            var mimeType = mimeTypes[extension];
                            response.writeHead(200, mimeType);
                            response.write(data);
                            response.end();
                        } else {
                            response.writeHead(200, {"Content-Type": "text/plain"});
                            response.write('Error de lectura en el fichero: ' + uri);
                            response.end();
                        }
                    });
                } else {    // Una vez dentro de cliente.html
                    while (uri.indexOf('/') === 0) { // Obtenemos la url de la petición
                        uri = uri.slice(1);
                        console.log("uri:" + uri);
                    }

                    var params = uri.split("/"); // la pasamos a array

                    response.writeHead(200, {"Content-Type": "text/plain"});
                    var result = '404 Not Found\n';

                    if (params[1] === 'temp' || params[1] === 'luz' || params[1] === 'humedad'){
                        console.log("Peticion REST sensors: " + uri);
                        var valor = parseFloat(params[2]);
                        setValor(params[1], valor);

                        result = getValor(params[1]);

                    } else if (params[1] === 'ac') {
                        console.log("ac1: " + params);
                        result = params[2];
                        console.log("ac2: " + result.toString());
                        if (result === 'ON') {
                            console.log('Encendemos el Aire Acondicionado.');
                        } else if (result === 'OFF') {
                            console.log('Apagamos el Aire Acondicionado.');
                        }


                    } else if (params[1] === 'aspersor') {
                        console.log("aspersor1: " + params);
                        result = params[2];
                        console.log("aspersor2: " + result.toString());
                        if (result === 'ON') {
                            console.log('Encendemos el Aspersor.');
                        } else if (result === 'OFF') {
                            console.log('Apagamos el Aspersor.');
                        }


                    } else if (params[1] === 'interruptor') {
                        console.log("interruptor: " + params);
                        result = params[2];

                        if (result === 'ON') {
                            console.log('Encendemos las luces.');
                        } else if (result === 'OFF') {
                            console.log('Apagamos las luces.');
                        }


                    } else if (params[1] === 'persiana') {
                        result = params[2];
                        if (result === 'Subida') {
                            console.log('Subimos las persianas');
                        } else if (result === 'Bajada') {
                            console.log('Bajamos las persianas');
                        }


                    } else if (params.length >= 4) { //Respuestas de cliente
                        console.log("Peticion REST Client: " + uri);
                        var val1 = parseFloat(params[1]);
                        var val2 = parseFloat(params[2]);

                        result = 'Operaciones cliente';


                    }

                    response.write(result.toString());
                    response.end();
                }
            });
        }
);






// Creando MongoDB

  MongoClient.connect("mongodb://localhost:27017/", function(err, db) {
	if(!err){
		console.log("Conectando a Base de Datos");
	}

	var dbo = db.db("pruebaBaseDatos");
	var msgCliente = null;
	httpServer.listen(8080);
	var io = socketio.listen(httpServer);

    dbo.createCollection("test", function (err, collection) {
      if(!err){
        console.log("Colección creada en Mongo: " + collection.collectionName);
      }
    });
 
	dbo.createCollection("dispositivos", function (err, collection) {
      if(!err){
        console.log("Colección creada en Mongo: " + collection.collectionName);
      }
    });
    io.sockets.on('connection',
            function (client) {
		client.on('output-evt', function (data) {
			
			identificador = client.id;
			console.log("Mensaje Recibido: " + data + "identificador de cliente" + identificador);		
			
			dbo.collection("dispositivos").insert({identificador: identificador, address:client.request.connection.remoteAddress, port:client.request.connection.remotePort, recibidoCliente: data}, {safe:true}, function(err, result) {
			
				if(!err){
					console.log("Insertado en colección de Mongo: address: "+ client.request.connection.remoteAddress + " port: "+ client.request.connection.remotePort + " recibidoCliente: " + data);

					dbo.collection("dispositivos").find().toArray(function(err, results){
						io.sockets.emit('all-connections', results);
						io.sockets.emit('all-update', {luz: lumens, temp: termostato, humedad: humedad});
					});
				} 
				else{
					console.log("Error al insertar datos en la colección.");
				}
			});	
		});
			client.on('disconnect', function() {
				identificador = client.id;

				console.log("Cliente id: " + identificador);
				dbo.collection("dispositivos").findOneAndDelete({identificador: identificador}, {safe:true}, function(err, result) {
					if(!err){
						io.sockets.emit('all-connections', result);
						console.log('El usuario registrado con marca de tiempo: '+identificador+' se ha desconectado');
					}
					else{
						console.log('El usuario registrado con marca de tiempo: '+identificador + 'no se ha podido desconectar');
					}

				});
		});
	});

    dbo.createCollection("historial", function (err, collection) {
        io.sockets.on('connection',
                function (client) {
                    // Control del Agente
                    client.on('add_historial', function (data) {
                        dbo.collection("test").insert(data, {safe: true}, function (err, result) {
						
					if (!err) {
						console.log("NO HAY ERROR");
						if (data.tipo === "temp") {
							setValor("temp", data.temp);
						}
						else if (data.tipo === "luz") {
							setValor("luz", data.luz);
						}
						else if (data.tipo === "humedad") {
							setValor("humedad", data.humedad);

						}
						io.sockets.emit('all-update', {luz: lumens, temp: termostato, humedad: humedad});
						var code = controlAgente();
						if (code > 0) {
							console.log("Hay un aviso de tipo: "+code);
						}
						if (code === 1) {
							io.emit('warning_alert', 1);
						} else if (code === 2) {
							io.emit('warning_alert', 2);
						} else if (code === 3) {
							io.emit('warning_alert', 3);
						} else if (code === 4){
							io.emit('warning_alert', 4)
						} else if (code === 5){
							io.emit('warning_alert', 5)
						}
					}
					
					});
					
                        
                    });


                });
    });

	console.log("Servicio MongoDB iniciado");

});



