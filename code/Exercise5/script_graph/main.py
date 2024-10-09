import pandas as pd
import plotly.express as px
import sys

def main(args: list[str]):

    maindf = pd.DataFrame()

    for path in args[1:]:
        df = pd.read_csv(path,sep=';',header=None)
        print(df.shape)
        column_to_add = df[[2]]
        column_to_add.columns = [path]
        maindf = pd.concat([maindf, column_to_add], axis=1)

    print(maindf.describe())

    df_melted = maindf.melt(var_name='Category', value_name='Value')

    # # Sample data
    # data = {
    #     "Category": ["A", "A", "A", "B", "B", "B", "C", "C", "C"],
    #     "Value": [10, 15, 14, 22, 24, 19, 30, 35, 31]
    # }
    # df = pd.DataFrame(data)

    # # Create the boxplot
    fig = px.box(df_melted, x='Category', y='Value', title='Box Plots of Different Datasets')
    fig.show()


if __name__ == "__main__":

    if len(sys.argv) < 2:
            print("Usage: python script.py <cc_table_path>...")
            sys.exit(1)

    main(sys.argv)
