import subprocess

output = subprocess.run(
    ["./app/build/install/app/bin/app", "-i=true", "-t=1"],
    stdout=subprocess.PIPE,
    text=True,
).stdout

with open("./integrationTests/ingredientTest.expected") as f:
    expected = f.read()

if output != expected:
    print(f"Expected:\n{expected}\nbut got:\n{output}")
    exit(1)