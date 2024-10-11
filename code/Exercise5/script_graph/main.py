import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
import sys

def box_plot(args: list[str]):

    maindf = pd.DataFrame()

    for path in args[1:]:
        df = pd.read_csv(path,sep=';',header=None)
        column_to_add = df[[2]]
        column_to_add.columns = [path]
        maindf = pd.concat([maindf, column_to_add], axis=1)

    df_melted = maindf.melt(var_name='Codebase', value_name='Complexity Cyclomatic')

    fig = px.box(df_melted, x='Codebase', y='Complexity Cyclomatic', title='Box Plots of the Cyclomatic Complexity in Different Codebase')
    fig.show()




def histogram(args: list[str]):

    maindf = pd.DataFrame()

    fig = go.Figure()
    for path in args[1:]:
        df = pd.read_csv(path,sep=';',header=None)
        column_to_add = df[[2]]
        column_to_add.columns = [path]
        maindf = pd.concat([maindf, column_to_add], axis=1)
        fig.add_trace(go.Histogram(
            x=column_to_add[path].to_numpy(),
            name=path, # name used in legend and hover labels
            opacity=0.75
        ))

    fig.show()


if __name__ == "__main__":

    if len(sys.argv) < 2:
            print("Usage: python script.py <cc_table_path>...")
            sys.exit(1)

    histogram(sys.argv)
    box_plot(sys.argv)
