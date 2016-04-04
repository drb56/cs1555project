import sys
import os
import loremipsum
import random
import re
from datetime import datetime


#if len(sys.argv) < 2:
#	raise Exception("not enough arguments!")

#num_users = sys.argv[1]


fnames = ['Deonna', 'Imogene', 'Serina', 'Mohammad', 'Garfield', 'Eulalia', 'Antone', 'Tamika', 'Marilynn', 'Jade', 'Maurice', 'Taisha', 'Therese', 'Dannette', 'Minta', 'Stephine', 'Helen', 'Colette', 'Armand', 'Asha', 'Lia', 'Christy', 'Glayds', 'Masako', 'Emilia', 'Elba', 'Antonietta', 'Leigha', 'Alyce', 'Earlene', 'Denisha', 'Edwardo', 'Lindsy', 'Armida', 'Tiffany', 'Crystal', 'Alecia', 'Zane', 'Anglea', 'Kristal', 'Esmeralda', 'Kanesha', 'Jessi', 'Will', 'Lyle', 'Ilse', 'Yvone', 'Katrice', 'Donna', 'Debera', 'Cassandra', 'Ji', 'Star', 'Arlette', 'Neoma', 'Rina', 'Olinda', 'Lovetta', 'Katheryn', 'Shellie', 'Sybil', 'Cathrine', 'Shaunta', 'Carson', 'Elicia', 'Lovella', 'Kristeen', 'Coleman', 'Ellis', 'Rafaela', 'Hong', 'Waylon', 'Soraya', 'Fransisca', 'Josefine', 'Cyndy', 'Leana', 'Farah', 'Susanne', 'Ami', 'Leonardo', 'Marcos', 'Pasquale', 'Hanh', 'Stephine', 'Zoe', 'Chanda', 'Malka', 'Blanca', 'Taunya', 'Zoraida', 'Stanley', 'Kathey', 'Geraldo', 'Emerald', 'Rosalyn', 'Deborah', 'Elroy', 'Marielle', 'Shondra', 'Wendie', 'Mercy', 'Allegra', 'Mickie', 'Robbie', 'Corazon', 'Kaci', 'Yolande', 'Arline', 'Vanessa', 'Thaddeus', 'Rocio', 'Yaeko', 'Sandie', 'Gayle', 'Vesta', 'Alyse', 'Siobhan', 'Graham', 'Dennise', 'Abe', 'Lorelei', 'Shawanda', 'Tandy', 'Jung', 'Edison', 'Kimberli', 'Alice', 'Naomi', 'Regan', 'Lonnie', 'Salley', 'Meg', 'Dillon', 'Spencer', 'Arnulfo', 'Talia', 'Ricki', 'Adaline', 'Claudette', 'Burma', 'Mira', 'Zina', 'Malorie', 'Hanh', 'Lashaun', 'Michell', 'Chas', 'Karla', 'Willette', 'Tory', 'Margit', 'Cammy', 'Marcellus', 'Mistie', 'Raisa', 'Aisha', 'Concha', 'Victoria', 'Nathalie', 'Pearlie', 'Yael', 'Niki', 'Hilary', 'Laronda', 'Susana', 'Enriqueta', 'Ashton', 'Apryl', 'Maryalice', 'Altha', 'Lisabeth', 'Hosea', 'Basil', 'January', 'Janel', 'Gwyn', 'Shelli', 'Avery', 'Golda', 'Norah', 'Eleonore', 'Savanna', 'Karan', 'Chantal', 'Miss', 'Cliff', 'Bulah', 'Halina', 'Kristie', 'Kim', 'Jetta', 'Catheryn', 'Angelo', 'Marvel', 'Gabriele', 'Stephan', 'Jarvis', 'Kary', 'Paris', 'Brenna', 'Melany', 'Clementine', 'Donnell', 'Nakita', 'Evelia', 'Alfonso', 'Kimberly', 'Caprice', 'Ethelyn', 'Kirstin', 'Carlie', 'Agustina', 'Kirsten', 'Elene', 'Liane', 'Ismael', 'Leland', 'Delbert', 'Nakia', 'Edwin', 'Ivonne', 'Annabelle', 'Shaunte', 'Reiko', 'Jeff', 'Ginger', 'Lamont', 'Lionel', 'Jamie', 'Marcy', 'Hans', 'Valery', 'Julio', 'Dianna', 'Verona', 'Estefana', 'Kristy', 'Millicent', 'Fatima', 'Genie', 'Shawn', 'Mamie', 'Anibal', 'Chante', 'Gricelda', 'Alonzo', 'Jeanette', 'Dung', 'Barabara', 'Valrie', 'Jami', 'Bonnie', 'Bibi', 'Edie', 'Dionna', 'Barbie', 'Sondra', 'Dorotha', 'Genaro', 'Mollie', 'Marceline', 'Zachary', 'Erick', 'Bennie', 'Ana', 'Lorena', 'Desire', 'Tanisha', 'Jenette', 'Renea', 'Shirly', 'Juan', 'Caren', 'Alaina', 'Cameron', 'Long', 'Jose', 'Lolita', 'Nancey', 'Synthia', 'Beata', 'Adelaida', 'Omega', 'Susy', 'Yoshiko', 'Victor', 'Tracey', 'Pearline', 'Willow', 'Nerissa', 'Shawanda', 'Adolfo', 'Ramona', 'Oneida', 'Velvet', 'Lance', 'Janelle', 'Renaldo', 'Leticia', 'Elfreda', 'Coreen', 'Forest', 'Santa', 'Ronni', 'Erich', 'Adrien', 'Christy', 'Sheryl', 'Jonnie', 'Odette', 'Loretta', 'Ferne', 'Bryon', 'Susann', 'Gaylene', 'Roxy', 'Ted', 'Tempie', 'Pura', 'Tracie', 'Marie', 'Georgeanna', 'Epifania', 'Quinn', 'Janella', 'Bryant', 'Kyla', 'Wanita', 'Nichol', 'Mathilde', 'Loraine', 'Cleveland', 'Arie', 'Athena', 'Kathrin', 'Miguel', 'Kyra', 'Mirna', 'Latesha', 'Morton', 'Curt', 'Angelyn', 'Homer', 'Rosemary', 'Svetlana', 'Izetta', 'Harvey', 'Valorie', 'Gillian', 'Lonny', 'Salvatore', 'Marian', 'Michale', 'Barbara', 'Dionne', 'Francine', 'Malika', 'Augustine', 'Rema', 'Shenita', 'Debby', 'Veola', 'Marianna', 'Lynwood', 'Yvette', 'Wilmer', 'Tona', 'Dacia', 'Lila', 'Priscilla', 'Tyra', 'Milda', 'Willette', 'Jena', 'Carlton', 'Jettie', 'Venessa', 'Isiah', 'Elmo', 'Terisa', 'Betty', 'Ileana', 'Emory', 'Georgette', 'Cary', 'Josiah', 'Laci', 'Markita', 'Emmaline', 'Jess', 'Audra', 'Bebe', 'Ayesha', 'Mindy', 'Elinor', 'Shanelle', 'Phylis', 'Britteny', 'Riva']

lnames = ['Munoz', 'Phillips', 'Chandler', 'Silva', 'Herring', 'Parks', 'Waters', 'Sexton', 'Buckley', 'Gibson', 'Velez', 'Mcpherson', 'Harrington', 'Garza', 'Calhoun', 'Duke', 'Willis', 'Conrad', 'Riddle', 'Howe', 'Bass', 'Alvarez', 'Shelton', 'Potter', 'Gentry', 'Swanson', 'Durham', 'Beasley', 'Raymond', 'Gomez', 'Michael', 'Rivas', 'Cohen', 'Sosa', 'Conner', 'Friedman', 'Crane', 'Case', 'Robbins', 'Doyle', 'Woodward', 'Morgan', 'Buchanan', 'Sweeney', 'Todd', 'Hartman', 'Cantu', 'Solomon', 'Powers', 'Blackburn', 'Farley', 'Cline', 'Tate', 'Mathis', 'Giles', 'Schultz', 'Winters', 'Morse', 'Skinner', 'Golden', 'Zuniga', 'Mckinney', 'Lucas', 'Meza', 'Mcdowell', 'Mason', 'Horne', 'Morris', 'Burgess', 'Love', 'Mullins', 'Parker', 'Vance', 'Barry', 'Li', 'Hunt', 'Wilcox', 'Yang', 'Savage', 'Flynn', 'Matthews', 'Underwood', 'Erickson', 'Randolph', 'Berger', 'Gordon', 'Newton', 'Cameron', 'Wade', 'Atkinson', 'Cole', 'Avila', 'Hudson', 'Frank', 'Houston', 'Valencia', 'Stark', 'Moreno', 'Ford', 'Sanchez', 'Kline', 'Nichols', 'Jacobs', 'Jordan', 'Blankenship', 'Church', 'Arroyo', 'Malone', 'Contreras', 'Short', 'Noble', 'Dillon', 'Barnett', 'Becker', 'Dickson', 'Bradshaw', 'Pearson', 'Blanchard', 'Frye', 'Fischer', 'Baldwin', 'Sims', 'Madden', 'Walsh', 'Green', 'Cooley', 'Spence', 'Joseph', 'George', 'Levy', 'Sheppard', 'Stanton', 'Clements', 'Beard', 'Harper', 'Galloway', 'Donaldson', 'Faulkner', 'Singh', 'Watts', 'Liu', 'Barrett', 'Kim', 'Rocha', 'Copeland', 'Beltran', 'Cherry', 'Tanner', 'Burton', 'Taylor', 'Mccullough', 'Charles', 'Holland', 'Steele', 'Cobb', 'Brennan', 'Mahoney', 'Harding', 'Ho', 'Norman', 'Roberson', 'Davies', 'Miles', 'Marks', 'Fields', 'Collier', 'Shields', 'Murphy', 'Carey', 'Peterson', 'Watkins', 'Bates', 'Fowler', 'Soto', 'Calderon', 'Edwards', 'Roy', 'Wells', 'Travis', 'Kidd', 'Santana', 'Collins', 'Barajas', 'Sutton', 'Hughes', 'Johnson', 'Horton', 'Simon', 'Oneal', 'Rosario', 'Pineda', 'Sawyer', 'Gilbert', 'Lynch', 'Cunningham', 'Mcclain', 'Davenport', 'Reynolds', 'Mccann', 'Gillespie', 'Gaines', 'Hayes', 'Moyer', 'Hale', 'Walters', 'Lam', 'Serrano', 'Wolf', 'Stevens', 'Reese', 'Butler', 'Wang', 'Sparks', 'Bullock', 'Schneider', 'Odom', 'Buck', 'Alexander', 'Pruitt', 'Price', 'Deleon', 'Forbes', 'Schaefer', 'Browning', 'Stone', 'Rush', 'Cabrera', 'Holloway', 'Wall', 'Grimes', 'Douglas', 'Moore', 'Santos', 'Carney', 'Sandoval', 'Vaughn', 'Torres', 'Burns', 'Smith', 'Singleton', 'Cowan', 'Solis', 'Holden', 'Suarez', 'Byrd', 'Bray', 'Dawson', 'Mccormick', 'Moss', 'Koch', 'Orr', 'Foster', 'Chen', 'Brandt', 'Valdez', 'Benson', 'Whitney', 'Logan', 'Austin', 'Davila', 'Lamb', 'Martin', 'Fry', 'Hebert', 'Carter', 'Branch', 'Lambert', 'Hobbs', 'Kelly', 'Molina', 'Compton', 'Gill', 'Oliver', 'Maddox', 'Cervantes', 'Lester', 'Klein', 'Brady', 'Gates', 'Maynard', 'Daniel', 'Wagner', 'Miranda', 'Kent', 'Sellers', 'Le', 'Werner', 'Khan', 'King', 'May', 'Carroll', 'Good', 'Hicks', 'Ingram', 'Marsh', 'Salinas', 'Peters', 'Gray', 'Sullivan', 'Cummings', 'Farmer', 'Hunter', 'Hahn', 'Harrison', 'Warner', 'Weeks', 'Long', 'Barton', 'Mendoza', 'Rivers', 'Castro', 'Knapp', 'Strickland', 'Vazquez', 'Hanna', 'Frey', 'Townsend', 'Donovan', 'Ware', 'Bailey', 'Lane', 'Perez', 'Bolton', 'English', 'Jennings', 'Horn', 'Hoover', 'Bowers', 'Hamilton', 'Shaw', 'Hancock', 'Bell', 'Marshall', 'Fisher', 'Spears', 'York', 'Santiago', 'Valenzuela', 'Gutierrez', 'Campbell', 'Vega', 'Poole', 'Hammond', 'Holmes', 'Owen', 'Gallagher', 'Chase', 'Christensen', 'Morrow', 'Garrett', 'Guerra', 'Ramsey', 'Knox', 'Meadows', 'Bentley', 'Espinoza', 'Wright', 'Randall', 'Day', 'Griffith', 'Rubio', 'Tran', 'Walker', 'Walton', 'Snow', 'Cochran', 'Kemp', 'Joyce', 'Mercado', 'Perry', 'Park', 'Frazier', 'Levine', 'Carlson', 'Carrillo', 'Heath', 'Roberts', 'Stanley', 'Bird', 'Mayer', 'Krause', 'Delacruz', 'Vang', 'Rivera', 'Navarro', 'Villa', 'Hart', 'Eaton', 'Huff', 'Hardy', 'Cisneros', 'Macias', 'Nunez', 'Francis', 'Tucker', 'Haley', 'Cooke', 'Diaz', 'Watson', 'Jarvis']

groups = ['baseball', 'football', 'soccer', 'movies', 'tv shows', 'anime', 'video games', 'league of legends', 'otters', 'pitt class of 2016', 'japanese', 'german', 'french', 'sweedish', 'artists', 'writers', 'book club', 'tennis', 'sewing', 'crochet', 'running', 'tea', 'beers', 'red pandas']

things = ['water', 'bottle', 'newspaper', 'rug', 'greeting', 'card', 'toilet', 'sofa', 'canvas', 'street', 'lights', 'USB', 'drive', 'cat', 'milk', 'desk', 'face', 'wash', 'paper', 'spoon', 'chalk', 'bag', 'table', 'flowers', 'needle', 'drill', 'press', 'bananas', 'white', 'out', 'glow', 'stick', 'nail', 'file', 'lamp', 'cinder', 'block', 'packing', 'peanuts', 'pencil', 'scotch', 'tape', 'photo', 'album', 'clamp', 'flag', 'grid', 'paper', 'keys', 'knife', 'stop', 'sign', 'earser', 'blouse', 'camera', 'mouse', 'pad', 'ring', 'money', 'sharpie', 'monitor', 'apple', 'paint', 'brush', 'screw', 'cell', 'phone', 'window', 'towel', 'buckel', 'lip', 'gloss', 'bookmark', 'glass', 'chapter', 'book', 'wallet', 'headphones', 'mop', 'tomato', 'hair', 'tie', 'floor', 'sun', 'glasses', 'couch', 'stockings', 'speakers', 'shoes', 'clock', 'coasters', 'shovel', 'eye', 'liner', 'checkbook', 'helmet', 'sponge', 'credit', 'card', 'chair', 'beef', 'boom', 'box', 'piano', 'hanger', 'candy', 'wrapper', 'plate', 'nail', 'clippers', 'model', 'car', 'bottle', 'cap', 'air', 'freshener', 'bracelet', 'cup', 'sketch', 'pad', 'outlet', 'rusty', 'nail', 'sand', 'paper', 'house', 'bottle', 'bread', 'magnet', 'door', 'thermostat', 'thermometer', 'spring', 'shawl', 'pen', 'thread', 'ipod', 'wagon', 'truck', 'shoe', 'lace', 'phone', 'drawer', 'car', 'bowl', 'plastic', 'fork', 'fake', 'flowers', 'candle', 'puddle', 'food', 'twister', 'lotion', 'clay', 'pot', 'pillow', 'pool', 'stick', 'leg', 'warmers', 'soda', 'can', 'mp3', 'player', 'fork', 'slipper', 'doll', 'picture', 'frame', 'rubber', 'band', 'radio', 'deodorant', 'teddies', 'lace', 'conditioner', 'hair', 'brush', 'pants', 'clothes', 'sidewalk', 'toothbrush', 'tooth', 'picks', 'cookie', 'jar', 'shampoo', 'bed', 'television', 'key', 'chain', 'washing', 'machine', 'socks', 'twezzers', 'sticky', 'note', 'remote', 'chocolate', 'vase', 'brocolli', 'sailboat', 'carrots', 'keyboard', 'sandal', 'tv', 'controller', 'mirror', 'glasses', 'perfume', 'video', 'games', 'soap', 'bow', 'fridge', 'watch', 'zipper', 'tire', 'swing', 'blanket', 'tree', 'shirt', 'box', 'charger', 'lamp', 'shade', 'playing', 'card', 'seat', 'belt', 'CD', 'toe', 'ring', 'soy', 'sauce', 'packet', 'computer', 'ice', 'cube', 'tray', 'tissue', 'box', 'rubber', 'duck', 'button', 'toothpaste', 'cork', 'book', 'purse', 'balloon']


insert_users = []

for i in range(100):

	rand_fname = random.randint(0, len(fnames)-1)
	fname = fnames[rand_fname]

	rand_lname = random.randint(0, len(lnames)-1)
	lname = lnames[rand_fname]

	email = fname + lname + "@gmail.com"


	year = random.randint(1970, 2000)
	month = random.randint(1, 12)
	day = random.randint(1, 28) #fuck months with 29 or 30 days.  Who needs them?

	date_of_birth = datetime(year, month, day)

	year = random.randint(2015, 2016)
	month = random.randint(1, 12)
	day = random.randint(1, 28)
	hour = random.randint(0, 23)
	minute = random.randint(0, 59)
	second = random.randint(0, 59)

	last_login = datetime(year, month, day, hour, minute, second)


	insert_users.append("INSERT INTO Users VALUES ('{}', '{}', '{}', TO_DATE('{}', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS'), NULL);".format(
			fname,
			lname,
			email,
			str(date_of_birth),
			str(last_login)
		))

insert_friendships = []

existing_friendships = []

for i in range(200):


	first_user = random.randint(1, len(insert_users))
	second_user = random.randint(1, len(insert_users))

	key = str(first_user) + " " + str(second_user)

	while key in existing_friendships:
		first_user = random.randint(1, len(insert_users))
		second_user = random.randint(1, len(insert_users))
		key = str(first_user) + " " + str(second_user)
		

	existing_friendships.append(key)

	year = random.randint(2015, 2016)
	month = random.randint(1, 12)
	day = random.randint(1, 28)
	hour = random.randint(0, 23)
	minute = random.randint(0, 59)
	second = random.randint(0, 59)

	friendDate = datetime(year, month, day, hour, minute, second)

	friendStatus = 1 if random.random() > .15 else 0



	insert_friendships.append("INSERT INTO Friends VALUES (TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS'), {}, {}, {}, NULL);".format(
		friendDate,
		friendStatus,
		first_user,
		second_user
	))

insert_groups = []
selected_groups = []

for i in range(10):


	rand_group = random.randint(0, len(groups)-1)
	while rand_group in selected_groups:
		rand_group = random.randint(0, len(groups)-1)
	selected_groups.append(rand_group)
	name = groups[rand_group]

	description = "group description " + str(i)

	personLimit = random.randint(0, 300)


	insert_groups.append("INSERT INTO Groups VALUES ('{}', '{}', {}, NULL);".format(
		name,
		description,
		personLimit
	))

messages = []

for i in range(300):
	sent = loremipsum.get_sentence()

	matches = re.findall(r'[A-Za-z]+', sent)

	matches = list(filter(lambda a: a != 'b' and a != 'B', matches))

	#capitalize
	matches[0] = matches[0][0].upper() + matches[0][1:]

	message = ' '.join(matches)
	if len(message) >= 100:
		message = message[:99]+'.'
	else:
		message = message + '.'

	rand_thing = random.randint(0, len(things)-1)
	subject = things[rand_thing]

	msg_text = message


	year = random.randint(2015, 2016)
	month = random.randint(1, 12)
	day = random.randint(1, 28)
	hour = random.randint(0, 23)
	minute = random.randint(0, 59)
	second = random.randint(0, 59)

	date_sent = datetime(year, month, day, hour, minute, second)

	sender_id = random.randint(1, len(insert_users))

	recipient_id = random.randint(1, len(insert_users))


	messages.append("INSERT INTO Messages VALUES ('{}', '{}', TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS'), {}, {}, NULL);".format(
		subject,
		msg_text,
		date_sent,
		sender_id,
		recipient_id
	))



#set the filename to be this script's directory + inputs.sql
filename = os.path.dirname(os.path.realpath(__file__)) + "/inputs.sql"

#if the file exists, set it to length 0
if os.path.exists(filename):
    os.remove(filename)

#open the file
with open(filename, 'w') as file:

	for user in insert_users:
		file.write(user + '\n')

	file.write('\n')

	for friendship in insert_friendships:
		file.write(friendship + '\n')

	file.write('\n')

	for message in messages:
		file.write(message + '\n')

	file.write('\n')

	for group in insert_groups:
		file.write(group + '\n')
