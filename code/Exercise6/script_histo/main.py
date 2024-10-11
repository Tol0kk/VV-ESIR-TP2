import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
import sys

def box_plot(args: list[str]):

    maindf = pd.DataFrame()

    for path in args[1:]:
        df = pd.read_csv(path,sep=';',header=0)
        column_to_add = df[['TCC']].dropna()
        column_to_add.columns = [path]
        maindf = pd.concat([maindf, column_to_add], axis=1)

    df_melted = maindf.melt(var_name='Codebase', value_name='TCC')

    fig = px.box(df_melted, x='Codebase', y='TCC', title='Box Plots of the Tight Class Cohesion')
    fig.show()




def histogram(args: list[str]):

    maindf = pd.DataFrame()

    fig = go.Figure()
    for path in args[1:]:
        df = pd.read_csv(path,sep=';',header=0)
        column_to_add = df[['TCC']]
        column_to_add.columns = [path]
        maindf = pd.concat([maindf, column_to_add], axis=1)

        nb_bins = 15


        fig.add_trace(go.Histogram(
            x=column_to_add[path].to_numpy(),
            name=path, # name used in legend and hover labels
            opacity=0.75,
            nbinsx=nb_bins
        ))

    fig.update_layout(
        title="Histogramme de TCC",  
        xaxis_title="valeurs de TCC",
        yaxis_title="Fréquence d'apparition"
    )

    fig.show()


if __name__ == "__main__":

    if len(sys.argv) < 2:
            print("Usage: python script.py <cc_table_path>...")
            sys.exit(1)

    histogram(sys.argv)
    box_plot(sys.argv)
