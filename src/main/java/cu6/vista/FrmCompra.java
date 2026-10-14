package cu6.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import cu6.controlador.ControlCompra;
import cu6.modelo.Compra;
import cu6.modelo.Producto;
import cu6.modelo.RenglonCompra;
import cu6.modelo.Tarjeta;
import cu6.modelo.Ticket;
import cu6.modelo.Tienda;
import observador.Observador;

/**
 * Ventana para comprar productos.
 *
 * @author Gustavo Romero
 */
public class FrmCompra extends JFrame implements Observador {

    private static final long serialVersionUID = 1L;
    private static final NumberFormat MONEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final String LINEA = "-".repeat(56);

    private final Tienda modelo;
    private ControlCompra control;
    private JList<String> lstProductos;
    private DefaultListModel<String> modeloProductos;
    private JSpinner spnCantidad;
    private JButton btnAgregar;
    private JTable tblSeleccionados;
    private DefaultTableModel modeloSeleccionados;
    private JLabel lblTotal;
    private JTextField txtNumeroTarjeta;
    private JButton btnValidar;
    private JTextArea txtZonaPago;
    private JButton btnPagar;
    private JLabel lblMensaje;
    private List<Producto> productos = new ArrayList<>();

    public FrmCompra(Tienda modelo) {
        super("Tienda - Comprar producto");
        this.modelo = modelo;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // titulo
        JLabel lblTitulo = new JLabel("  Comprar producto");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(0, 82, 147));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setPreferredSize(new Dimension(100, 48));
        add(lblTitulo, BorderLayout.NORTH);

        // lista de productos
        modeloProductos = new DefaultListModel<>();
        lstProductos = new JList<>(modeloProductos);
        lstProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstProductos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        lstProductos.setFixedCellHeight(24);
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> {
            int indice = lstProductos.getSelectedIndex();
            if (indice < 0) {
                mostrarMensaje("Seleccione un producto de la lista.");
                return;
            }
            control.agregarProducto(productos.get(indice), (Integer) spnCantidad.getValue());
        });
        JPanel pnlAgregar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        pnlAgregar.add(new JLabel("Cantidad:"));
        pnlAgregar.add(spnCantidad);
        pnlAgregar.add(btnAgregar);
        JPanel pnlProductos = new JPanel(new BorderLayout());
        pnlProductos.setBorder(BorderFactory.createTitledBorder("Productos"));
        pnlProductos.add(new JScrollPane(lstProductos), BorderLayout.CENTER);
        pnlProductos.add(pnlAgregar, BorderLayout.SOUTH);
        pnlProductos.setPreferredSize(new Dimension(420, 300));

        // productos seleccionados
        modeloSeleccionados = new DefaultTableModel(new Object[]{"Producto", "Cant.", "Precio", "Subtotal"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblSeleccionados = new JTable(modeloSeleccionados);
        tblSeleccionados.setRowHeight(22);
        tblSeleccionados.getColumnModel().getColumn(0).setPreferredWidth(200);
        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int c = 1; c <= 3; c++) {
            tblSeleccionados.getColumnModel().getColumn(c).setCellRenderer(derecha);
        }
        lblTotal = new JLabel(" ", SwingConstants.RIGHT);
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 15f));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 8));
        JPanel pnlSeleccionados = new JPanel(new BorderLayout());
        pnlSeleccionados.setBorder(BorderFactory.createTitledBorder("Productos seleccionados"));
        pnlSeleccionados.add(new JScrollPane(tblSeleccionados), BorderLayout.CENTER);
        pnlSeleccionados.add(lblTotal, BorderLayout.SOUTH);

        JPanel pnlSuperior = new JPanel(new BorderLayout(10, 0));
        pnlSuperior.add(pnlProductos, BorderLayout.WEST);
        pnlSuperior.add(pnlSeleccionados, BorderLayout.CENTER);

        // tarjeta y ticket
        txtNumeroTarjeta = new JTextField(19);
        txtNumeroTarjeta.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        txtNumeroTarjeta.addActionListener(e -> control.validarTarjeta(txtNumeroTarjeta.getText()));
        btnValidar = new JButton("Consultar tarjeta");
        btnValidar.addActionListener(e -> control.validarTarjeta(txtNumeroTarjeta.getText()));
        JPanel pnlTarjeta = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        pnlTarjeta.add(new JLabel("Número de tarjeta:"));
        pnlTarjeta.add(txtNumeroTarjeta);
        pnlTarjeta.add(btnValidar);
        txtZonaPago = new JTextArea();
        txtZonaPago.setEditable(false);
        txtZonaPago.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        txtZonaPago.setMargin(new Insets(6, 10, 6, 10));
        btnPagar = new JButton("Pagar");
        btnPagar.addActionListener(e -> control.pagar());
        JPanel pnlBotonPagar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        pnlBotonPagar.add(btnPagar);
        JPanel pnlPago = new JPanel(new BorderLayout());
        pnlPago.setBorder(BorderFactory.createTitledBorder("Pago: datos de la tarjeta / Ticket de pago"));
        pnlPago.add(pnlTarjeta, BorderLayout.NORTH);
        pnlPago.add(new JScrollPane(txtZonaPago), BorderLayout.CENTER);
        pnlPago.add(pnlBotonPagar, BorderLayout.SOUTH);
        pnlPago.setPreferredSize(new Dimension(100, 320));

        JPanel pnlCentro = new JPanel(new BorderLayout(0, 8));
        pnlCentro.add(pnlSuperior, BorderLayout.CENTER);
        pnlCentro.add(pnlPago, BorderLayout.SOUTH);
        add(pnlCentro, BorderLayout.CENTER);

        // mensajes
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(lblMensaje.getFont().deriveFont(Font.BOLD, 13f));
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));
        add(lblMensaje, BorderLayout.SOUTH);

        limpiarPago();
        setSize(1000, 740);
        setMinimumSize(new Dimension(900, 680));
        setLocationRelativeTo(null);
    }

    public void setControl(ControlCompra control) {
        this.control = control;
    }

    // se llama cuando cambia el modelo
    @Override
    public void actualizar() {
        mostrarProductos(modelo.obtenerProductos());
        Compra compra = modelo.obtenerCompra();
        mostrarCompra(compra);
        Ticket ticket = modelo.getTicketActual();
        Tarjeta tarjeta = modelo.getTarjetaActual();
        if (ticket != null && compra.estaVacia()) {
            mostrarTicket(ticket);
        } else if (compra.estaVacia()) {
            limpiarPago();
        } else {
            solicitarTarjeta();
            if (tarjeta != null) {
                mostrarDatosTarjeta(tarjeta);
            } else {
                txtZonaPago.setText("Capture el número de tarjeta y presione \"Consultar tarjeta\".");
                habilitarPagar(false);
                if (modelo.getNumeroTarjetaBuscado() != null) {
                    // tarjeta no registrada
                    mostrarMensaje("La tarjeta no está registrada en el sistema de pagos.");
                }
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        lblMensaje.setForeground(new Color(180, 0, 0));
        lblMensaje.setText(mensaje);
    }

    private void mostrarProductos(List<Producto> productos) {
        int seleccionado = lstProductos.getSelectedIndex();
        this.productos = new ArrayList<>(productos);
        modeloProductos.clear();
        for (Producto producto : productos) {
            modeloProductos.addElement(String.format("%-25s%12s", producto.getNombre(),
                    MONEDA.format(producto.getPrecio())));
        }
        if (seleccionado >= 0 && seleccionado < productos.size()) {
            lstProductos.setSelectedIndex(seleccionado);
        }
    }

    private void mostrarCompra(Compra compra) {
        modeloSeleccionados.setRowCount(0);
        for (RenglonCompra renglon : compra.getRenglones()) {
            modeloSeleccionados.addRow(new Object[]{
                renglon.getProducto().getNombre(),
                renglon.getCantidad(),
                MONEDA.format(renglon.getProducto().getPrecio()),
                MONEDA.format(renglon.getSubtotal())
            });
        }
        lblTotal.setText(compra.estaVacia() ? " " : compra.getTotalArticulos() + " artículo(s)   Total (IVA incluido): "
                + MONEDA.format(compra.getTotal()));
    }

    private void solicitarTarjeta() {
        txtNumeroTarjeta.setEnabled(true);
        btnValidar.setEnabled(true);
        lblMensaje.setForeground(Color.DARK_GRAY);
        lblMensaje.setText("Agregue más productos o capture su número de tarjeta para pagar.");
    }

    private void mostrarDatosTarjeta(Tarjeta tarjeta) {
        StringBuilder sb = new StringBuilder();
        sb.append("DATOS DE LA TARJETA\n").append(LINEA).append('\n');
        sb.append(renglon("Banco emisor:", tarjeta.getBancoEmisor()));
        sb.append(renglon("Ciudad:", tarjeta.getCiudad()));
        sb.append(renglon("Número:", tarjeta.getNumeroEnmascarado()));
        txtZonaPago.setText(sb.toString());
        habilitarPagar(true);
        lblMensaje.setForeground(Color.DARK_GRAY);
        lblMensaje.setText("Verifique los datos de la tarjeta y presione \"Pagar\".");
    }

    // el ticket se muestra donde estaban los datos de la tarjeta
    private void mostrarTicket(Ticket ticket) {
        Tarjeta tarjeta = ticket.getTarjeta();
        Compra compra = ticket.getCompra();
        StringBuilder sb = new StringBuilder();
        sb.append("TICKET DE PAGO\n").append(LINEA).append('\n');
        sb.append(renglon("Folio:", ticket.getFolio()));
        sb.append(renglon("Fecha:", FORMATO_FECHA.format(ticket.getFecha())));
        sb.append(renglon("Tarjeta:", tarjeta.getNumeroEnmascarado()));
        sb.append(renglon("Banco emisor:", tarjeta.getBancoEmisor() + " (" + tarjeta.getCiudad() + ")"));
        sb.append(LINEA).append('\n');
        for (RenglonCompra r : compra.getRenglones()) {
            sb.append(String.format("%3d x %-28s%12s%n", r.getCantidad(), r.getProducto().getNombre(),
                    MONEDA.format(r.getSubtotal())));
        }
        sb.append(LINEA).append('\n');
        sb.append(String.format("%-34s%12s%n", "TOTAL (IVA incluido)", MONEDA.format(compra.getTotal())));
        txtZonaPago.setText(sb.toString());
        txtZonaPago.setCaretPosition(0);
        txtNumeroTarjeta.setText("");
        txtNumeroTarjeta.setEnabled(false);
        btnValidar.setEnabled(false);
        habilitarPagar(false);
        lblMensaje.setForeground(new Color(0, 120, 0));
        lblMensaje.setText("Pago realizado con folio " + ticket.getFolio() + ".");
    }

    private void habilitarPagar(boolean habilitar) {
        btnPagar.setEnabled(habilitar);
    }

    private void limpiarPago() {
        txtZonaPago.setText("Agregue productos y capture el número de tarjeta para pagar.");
        txtNumeroTarjeta.setText("");
        txtNumeroTarjeta.setEnabled(false);
        btnValidar.setEnabled(false);
        habilitarPagar(false);
        lblMensaje.setText(" ");
    }

    private static String renglon(String etiqueta, String valor) {
        return String.format("%-15s%s%n", etiqueta, valor);
    }
}
