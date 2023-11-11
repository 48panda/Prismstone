import os, json
path_to_json = os.getcwd()
json_files = [pos_json for pos_json in os.listdir(path_to_json) if pos_json.endswith('.json')]
types = ["heatstone","mendstone","datastone","soulstone","growstone","warpstone"]
moretypes = ["redstone","examplestone","heatstone","mendstone","datastone","soulstone","growstone","warpstone"]
for file in json_files:
  if "example" in file:
    with open(file) as f:
      text = f.read()
      for t in types:
        with open(file.replace("example", t), "w+") as F:
          F.write(text)
          
  if file == "wire_crossing.json":
    with open(file) as f:
      text = f.read()
      for t in moretypes:
        for u in moretypes:
          with open(f"wire_crossing_{t}_{u}.json", "w+") as F:
            F.write(text)
