import os
import json

class hp_database:
    def __init__(self):
        #Each entry of the list is a dictionary of a character
        self.hp_data = list()

    def load_hp_data(self,file_name):
        f = open(file_name,'r')
        raw_data = json.load(f)
        #We only keep the following information for each character
        data_we_want = ['name', 'species', 'gender', 'house', 'actor']
        for characters in raw_data:
            info = dict()
            for key in characters.keys():
                if key in data_we_want:
                    info[key] = characters[key]
            self.hp_data.append(info)
        f.close()

    def get_chars(self):
        # Get all characters from the list
        return self.hp_data

    def get_char(self, mid):
        # Get a specific character from mid
        try:
            char = self.hp_data[mid]
        except Exception as ex:
            char = None
        return char

if __name__ == "__main__":
    hp = hp_database()
    file_name = 'characters.json'
    hp.load_hp_data(file_name)
    print(hp.hp_data)
