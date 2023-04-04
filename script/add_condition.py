import builtins
import json
import os
from typing import Final

blacklist_namespace: Final[set[str]] = {"minecraft", "piercingpaxels", "c", "fabric"}


def get_last_values(dict_object: dict) -> set[str]:
    last_values = set()

    def match_type(object):
        match type(object):
            case builtins.str:
                last_values.add(object)
            case builtins.dict:
                last_values.update(get_last_values(object))
            case builtins.list:
                for item in object:
                    match_type(item)

    for _, value in dict_object.items():
        match_type(value)

    return last_values


condition_template = lambda modids: {
    "fabric:load_conditions": [
        {"condition": "fabric:all_mods_loaded", "values": modids}
    ]
}

for path, dirs, files in os.walk(
    "./src/main/resources/data/piercingpaxels/recipes/compat"
):
    path = path.replace("\\", "/")
    for file in files:
        filepath = f"{path}/{file}"
        with open(filepath) as file_ref:
            json_data = json.load(file_ref)
        modids = [*{
            i_
            for i in get_last_values(json_data)
            if ":" in i and (i_ := i.split(":")[0]) not in blacklist_namespace
        }]
        if modids:
            condition = condition_template(modids)
            json_data = (condition | json_data if 'fabric:load_conditions' not in json_data else json_data | condition)
        elif 'fabric:load_conditions' in json_data:
            del json_data['fabric:load_conditions']
        with open(filepath, "w") as file_ref:
            json.dump(json_data, file_ref, indent=2)
        print(filepath, condition, sep='\n\t', end='\n\n')
